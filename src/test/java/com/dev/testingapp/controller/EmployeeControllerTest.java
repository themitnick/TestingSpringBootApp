package com.dev.testingapp.controller;

import com.dev.testingapp.model.Employee;
import com.dev.testingapp.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    Employee employee = null;
    ResultActions  result;

    @BeforeEach
    void setup(){
        employee = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("tyrone@gmail.com")
                .build();
    }

    @Test
    public void createEmployeeTest() throws Exception{
        //given - precondition or setup
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
            .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        //when - action or the behavior that we are going test
        ResultActions result = mockMvc.perform(post("/api/employees")
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
        given(employeeService.getAllEmployees()).willReturn(employeeList);

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
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behavior that we are going test
        result = mockMvc.perform(get("/api/employees/{id}", employeeId));

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
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behavior that we are going test
        result = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateEmployeeSuccessTest() throws Exception{
        //given - precondition or setup
        long employeeId = 1L;
        Employee employeeSaved  = Employee.builder()
                .firstName("Tyrone")
                .lastName("YAO")
                .email("tyrone@gmail.com")
                .build();

        Employee employeeUpdated = Employee.builder()
                .firstName("Julien")
                .lastName("Konaté")
                .email("jkonate@gmail.com")
                .build();

        //when - action or the behavior that we are going test
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employeeSaved));
        given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        result = mockMvc.perform(put("/api/employees/{id}", employeeId)
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

        Employee employeeUpdated = Employee.builder()
                .firstName("Julien")
                .lastName("Konaté")
                .email("jkonate@gmail.com")
                .build();

        //when - action or the behavior that we are going test
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

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
        long employeeId = 1L;
        willDoNothing().given(employeeService).deletedEmployee(employeeId);

        //when - action or the behavior that we are going test
            result = mockMvc.perform(delete("/api/employee/{id}", employeeId));

        //then -verify the ouput
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
