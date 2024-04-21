package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeSalaryServiceTest {

    @Mock
    private EmployeeListService employeeListService;

    @InjectMocks
    private EmployeeSalaryService employeeSalaryService;

    @Test
    public void testGetHighestSalaryOfEmployees() throws ServiceException, EmployeeNotFoundException {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee.Builder().id("1").name( "John Doe").salary(50000).build());
        employees.add(new Employee.Builder().id("5").name( "John Doe").salary(60000).build());

        List<Employee> employees1 = new ArrayList<>();
        employees.add(new Employee.Builder().id("1").name( "Jane Doe").salary(20000).build());
        employees.add(new Employee.Builder().id("5").name( "Jane Doe").salary(80000).build());

        Map<String, List<Employee>> map = new HashMap<>();
        map.put("John Doe", employees);
        map.put("Jane Doe", employees1);
        when(employeeListService.getAllEmployees()).thenReturn(map);

        int highestSalary = employeeSalaryService.getHighestSalaryOfEmployees();
        assertEquals(80000, highestSalary);
    }

    @Test
    public void testGetTop10HighestEarningEmployeeNames() throws ServiceException, EmployeeNotFoundException {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee.Builder().id("1").name( "John Doe").salary(50000).build());
        employees.add(new Employee.Builder().id("5").name( "John Doe").salary(60000).build());

        List<Employee> employees1 = new ArrayList<>();
        employees.add(new Employee.Builder().id("1").name( "Jane Doe").salary(20000).build());
        employees.add(new Employee.Builder().id("5").name( "Jane Doe").salary(80000).build());

        Map<String, List<Employee>> map = new HashMap<>();
        map.put("John Doe", employees);
        map.put("Jane Doe", employees1);
        when(employeeListService.getAllEmployees()).thenReturn(map);

        List<String> top10EmployeeNames = employeeSalaryService.getTop10HighestEarningEmployeeNames();
        assertEquals(4, top10EmployeeNames.size());
        assertEquals("Jane Doe", top10EmployeeNames.get(0));
        assertEquals("John Doe", top10EmployeeNames.get(1));
    }

    @Test
    public void testGetTop10HighestEarningEmployeeNamesWithEmptyList() throws ServiceException, EmployeeNotFoundException {

        when(employeeListService.getAllEmployees()).thenReturn(new HashMap<>());

        List<String> top10EmployeeNames = employeeSalaryService.getTop10HighestEarningEmployeeNames();
        assertEquals(0, top10EmployeeNames.size());
    }

    @Test
    public void testGetTop10HighestEarningEmployeeNamesWithLessThan10Employees() throws ServiceException, EmployeeNotFoundException {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee.Builder().id("1").name( "John Doe").salary(50000).build());
        employees.add(new Employee.Builder().id("5").name( "John Doe").salary(60000).build());

        Map<String, List<Employee>> map = new HashMap<>();
        map.put("John Doe", employees);
        when(employeeListService.getAllEmployees()).thenReturn(map);

        List<String> top10EmployeeNames = employeeSalaryService.getTop10HighestEarningEmployeeNames();
        assertEquals(2, top10EmployeeNames.size());
        assertEquals("John Doe", top10EmployeeNames.get(0));
        assertEquals("John Doe", top10EmployeeNames.get(1));
    }

}
