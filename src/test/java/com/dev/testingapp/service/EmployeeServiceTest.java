package com.dev.testingapp.service;

import com.dev.testingapp.exception.ResourceNotFoundException;
import com.dev.testingapp.model.Employee;
import com.dev.testingapp.repository.EmployeeRepository;
import com.dev.testingapp.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("Tyrone")
                .lastName("YAO")
                .email("tyrone@gmail.com")
                .build();
    }

    @Test
    void saveEmployeeTest(){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then -verify the ouput
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    void saveEmployeeThrowExceptionTest(){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behavior that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then -verify the ouput
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getAllEmployeeListTest(){
        //given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("Tichou")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        //when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then -verify the ouput
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList).hasSize(2);
    }

    @Test
    void getAllEmployeeListFailTest(){
        //given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("Tichou")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then -verify the ouput
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isZero();
    }

    @Test
    void findEmployeeByIdTest(){
        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or the behavior that we are going test
        Employee foundEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then -verify the ouput
        Assertions.assertThat(foundEmployee).isNotNull();
    }

    @Test
    void updateEmployeeTest(){
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("yao@gmail.com");
        employee.setFirstName("Tichou");

        //when - action or the behavior that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then -verify the ouput
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Tichou");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("yao@gmail.com");
    }

    @Test
    void deleteEmployeeTest(){
        //given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //when - action or the behavior that we are going test
        employeeService.deletedEmployee(1L);
        Optional<Employee> deletedEmployee = employeeService.getEmployeeById(employeeId);

        //then -verify the ouput
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
