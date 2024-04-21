package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeSalaryService {

    private final EmployeeListService employeeDetailsService;

    public EmployeeSalaryService(@Autowired EmployeeListService employeeDetailsService){
        this.employeeDetailsService = employeeDetailsService;
    }
    public int getHighestSalaryOfEmployees() throws ServiceException, EmployeeNotFoundException {
        List<Employee> employees = getFlatListOfEmployee();
        employees.sort((e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary()));
        return employees.get(0).getSalary();
    }

    public List<String> getTop10HighestEarningEmployeeNames() throws ServiceException, EmployeeNotFoundException {
        List<Employee> employees = getFlatListOfEmployee();
        employees.sort((e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary()));
        List<String> top10EmployeeNames = new ArrayList<>();
        int count = Math.min(10, employees.size());
        for (int i = 0; i < count; i++) {
            top10EmployeeNames.add(employees.get(i).getName());
        }
        return top10EmployeeNames;
    }

    private List<Employee> getFlatListOfEmployee() throws ServiceException, EmployeeNotFoundException {
        return employeeDetailsService.getAllEmployees().values().stream()
                .flatMap(List::stream).collect(Collectors.toList());
    }
}