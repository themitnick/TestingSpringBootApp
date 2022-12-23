package com.dev.testingapp.integrationTests;

import com.dev.testingapp.model.Employee;
import com.dev.testingapp.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    Employee employee = null;
    ResultActions  result;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
        employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("tyrone@gmail.com")
                .build();
    }

    @Test
    public void createEmployeeTest() throws Exception{
        //given - precondition or setup

        //when - action or the behavior that we are going test
        result = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    public void getAllEmployeesTest() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Christian").lastName("CISSE").email("chrisso@yahoo.fr").build());
        employeeList.add(Employee.builder().firstName("Daniel").lastName("KOUDOU").email("daniel@yaoo.fr").build());
        employeeRepository.saveAll(employeeList);

        //when - action or the behavior that we are going test
        result = mockMvc.perform(get("/api/employees"));

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }

    @Test
    public void getEmployeeSuccessTest() throws Exception {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        result = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    public void getEmployeeFailTest() throws Exception{
        //given - precondition or setup
        long employeeId = 1L;
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        result = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateEmployeeSuccessTest() throws Exception{
        //given - precondition or setup
        Employee employeeSaved  = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("tyrone@gmail.com")
                .build();
        employeeRepository.save(employeeSaved);

        Employee employeeUpdated = Employee.builder()
                .firstName("Julien")
                .lastName("Konaté")
                .email("jkonate@gmail.com")
                .build();

        //when - action or the behavior that we are going test
        result = mockMvc.perform(put("/api/employees/{id}", employeeSaved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeUpdated))
        );

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employeeUpdated.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employeeUpdated.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employeeUpdated.getEmail())));

    }

    @Test
    public void updateEmployeeFailTest() throws Exception{
        //given - precondition or setup
        long employeeId = 1L;
        Employee employeeSaved  = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("tyrone@gmail.com")
                .build();
        employeeRepository.save(employeeSaved);

        Employee employeeUpdated = Employee.builder()
                .firstName("Julien")
                .lastName("Konaté")
                .email("jkonate@gmail.com")
                .build();

        //when - action or the behavior that we are going test
        result = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeUpdated))
        );

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteEmployeeTest() throws Exception{
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        result = mockMvc.perform(delete("/api/employee/{id}", employee.getId()));

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


}
