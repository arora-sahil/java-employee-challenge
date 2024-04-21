package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.InvalidRequestException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/employees")
@RestController
public interface IEmployeeController {

    @GetMapping()
    ResponseEntity<List<Employee>> getAllEmployees() throws IOException, ServiceException, EmployeeNotFoundException;

    @GetMapping("/search/{searchString}")
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) throws ServiceException, EmployeeNotFoundException;

    @GetMapping("/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable String id) throws ServiceException, EmployeeNotFoundException;

    @GetMapping("/highest-salary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees() throws ServiceException, EmployeeNotFoundException;

    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() throws ServiceException, EmployeeNotFoundException;

    @PostMapping()
    ResponseEntity<String> createEmployee(@RequestBody Map<String, Object> employeeInput) throws ServiceException, InvalidRequestException;

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id) throws ServiceException, EmployeeNotFoundException;

}
