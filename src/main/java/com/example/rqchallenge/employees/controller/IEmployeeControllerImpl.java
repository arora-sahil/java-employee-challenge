package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.InvalidRequestException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeCreateService;
import com.example.rqchallenge.employees.service.EmployeeDeleteService;
import com.example.rqchallenge.employees.service.EmployeeListService;
import com.example.rqchallenge.employees.service.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class IEmployeeControllerImpl implements IEmployeeController {

    @Autowired
    EmployeeCreateService employeeCreateService;

    @Autowired
    EmployeeListService employeeListService;

    @Autowired
    EmployeeDeleteService employeeDeleteService;

    @Autowired
    EmployeeSalaryService employeeSalaryService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws ServiceException, EmployeeNotFoundException {
        List<Employee> values = employeeListService.getAllEmployees().values().stream()
                .flatMap(List::stream).collect(Collectors.toList());
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString)
            throws EmployeeNotFoundException {
        return new ResponseEntity<>(employeeListService.search(searchString.trim()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id)
            throws ServiceException, EmployeeNotFoundException {
        return new ResponseEntity<>(employeeListService.getEmployeeById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() throws ServiceException, EmployeeNotFoundException {
        return new ResponseEntity<>(employeeSalaryService.getHighestSalaryOfEmployees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() throws ServiceException, EmployeeNotFoundException {
        return new ResponseEntity<>(employeeSalaryService.getTop10HighestEarningEmployeeNames(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createEmployee(Map<String, Object> employeeInput) throws ServiceException, InvalidRequestException {
        String status = employeeCreateService.create(employeeInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) throws ServiceException, EmployeeNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeDeleteService.deleteEmployeeById(id));
    }
}
