package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeCreateService;
import com.example.rqchallenge.employees.service.EmployeeDeleteService;
import com.example.rqchallenge.employees.service.EmployeeListService;
import com.example.rqchallenge.employees.service.EmployeeSalaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IEmployeeControllerImpl.class)
public class IEmployeeControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeCreateService employeeCreateService;

    @MockBean
    private EmployeeListService employeeListService;

    @MockBean
    private EmployeeDeleteService employeeDeleteService;

    @MockBean
    private EmployeeSalaryService employeeSalaryService;

    @Test
    public void testGetAllEmployees() throws Exception {
        Map<String, List<Employee>> map = new HashMap<>();
        when(employeeListService.getAllEmployees()).thenReturn(map);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {
        String searchString = "John";

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/search")
                        .param("searchString", searchString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        String employeeId = "123";
        Employee employee = new Employee(); // Create a test employee

        when(employeeListService.getEmployeeById(employeeId)).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()));
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {
        int highestSalary = 10000; // Test highest salary value

        when(employeeSalaryService.getHighestSalaryOfEmployees()).thenReturn(highestSalary);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/highest-salary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(highestSalary)));
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> topNames = new ArrayList<>();
        // Add test employee names to the list

        when(employeeSalaryService.getTop10HighestEarningEmployeeNames()).thenReturn(topNames);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/topTenHighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testCreateEmployee() throws Exception {
        String requestBody = "{\"name\":\"John Doe\",\"salary\":5000}"; // Test request body

        when(employeeCreateService.create(any(Map.class))).thenReturn("Employee created successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("Employee created successfully"));
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        String employeeId = "123";

        when(employeeDeleteService.deleteEmployeeById(employeeId)).thenReturn("Employee deleted successfully");

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));
    }
}
