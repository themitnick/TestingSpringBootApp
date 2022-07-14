package com.dev.testingapp.repository;

import com.dev.testingapp.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void saveEmployeeTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();

        //when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        //then -verify the ouput
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    public void findAllEmployeeTest(){
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Irène")
                .lastName("CISSE")
                .email("irene.cisse@gmail.com")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when - action or the behavior that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        //then -verify the ouput
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void findEmployeeByIdTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        Employee employeeById = employeeRepository.findById(employee.getId()).get();

        //then -verify the ouput
        assertThat(employeeById).isNotNull();
    }
    @Test
    public void findEmployeeEmailTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        Employee employeeByEmail = employeeRepository.findByEmail(employee.getEmail()).get();

        //then -verify the ouput
        assertThat(employeeByEmail).isNotNull();
        assertThat(employeeByEmail.getEmail()).isEqualTo("yao@gmail.com");
    }

    @Test
    public void updateEmployeeTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        Employee employeeSaved = employeeRepository.findById(employee.getId()).get();
        employeeSaved.setFirstName("Tichou");
        employeeSaved.setEmail("tyrone@gmail.com");
        Employee employeeUpdated =  employeeRepository.save(employeeSaved);

        //then -verify the ouput
        assertThat(employeeUpdated.getEmail()).isEqualTo("tyrone@gmail.com");
        assertThat(employeeUpdated.getFirstName()).isEqualTo("Tichou");
    }

    @Test
    public void deletedEmployeeTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then -verify the ouput
        assertThat(employeeOptional).isEmpty();
    }

    //JUnit test for custom query using JPQL with index
    @Test
    public void findEmployeeByFirstNameAndLastNameTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = "Tyrone";
        String lastName = "YAO";

        //when - action or the behavior that we are going test
        Employee employeeFound = employeeRepository.findByJPQL(firstName, lastName);

        //then -verify the ouput
        assertThat(employeeFound).isNotNull();
    }

    //JUnit test for custom query using JPQL with named params
    @Test
    public void findEmployeeByFirstNameAndLastNameUsingNamedParamsTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = "Tyrone";
        String lastName = "YAO";

        //when - action or the behavior that we are going test
        Employee employeeFound = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //then -verify the ouput
        assertThat(employeeFound).isNotNull();
    }

    //Junit test for custom query native SQL with index paramas
    @Test
    public void findEmployeeByFirstNameAndLastNameUsingNativeSQLTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = "Tyrone";
        String lastName = "YAO";

        //when - action or the behavior that we are going test
        Employee empployeeFound = employeeRepository.findByNativeSQL(firstName, lastName);

        //then -verify the ouput
        assertThat(empployeeFound).isNotNull();
    }

    @Test
    public void findEmployeeByFirstNameAndLastNameUsingNativeSQLNamedTest(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("yao@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = "Tyrone";
        String lastName = "YAO";

        //when - action or the behavior that we are going test
        Employee empployeeFound = employeeRepository.findByNativeSQLNamed(firstName, lastName);

        //then -verify the ouput
        assertThat(empployeeFound).isNotNull();
    }

}
