package com.dev.testingapp.service.impl;

import com.dev.testingapp.exception.ResourceNotFoundException;
import com.dev.testingapp.model.Employee;
import com.dev.testingapp.repository.EmployeeRepository;
import com.dev.testingapp.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Employee saveEmployee(Employee employee) {

    Optional<Employee> saveEmployee = employeeRepository.findByEmail(employee.getEmail());
    if (saveEmployee.isPresent()) {
      throw new ResourceNotFoundException(
          "An employee already exist with email: " + employee.getEmail());
    }

    return employeeRepository.save(employee);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  @Override
  public Optional<Employee> getEmployeeById(long id) {
    return employeeRepository.findById(id);
  }

  @Override
  public Employee updateEmployee(Employee employee) {
    return employeeRepository.save(employee);
  }

  @Override
  public void deletedEmployee(long id) {
    employeeRepository.deleteById(id);
  }
}
