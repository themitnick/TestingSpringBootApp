package com.dev.testingapp.repository;

import com.dev.testingapp.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    //@DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
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
}
