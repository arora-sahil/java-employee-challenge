package com.example.rqchallenge.employees.cache;

import com.example.rqchallenge.employees.cache.EmployeeCache;
import com.example.rqchallenge.employees.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeCacheTest {

    private EmployeeCache employeeCache;

    @BeforeEach
    public void setUp() {
        employeeCache = EmployeeCache.getInstance();
        employeeCache.getCache().clear(); // Clear cache before each test
    }

    @Test
    public void testPutAndGet() {
        String key = "department1";
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee.Builder().id("1").name("John Doe").salary( 1000).build());
        employees.add(new Employee.Builder().id("2").name("Jane Smith").salary(1200).build());

        employeeCache.put(key, employees);

        assertTrue(employeeCache.containsKey(key));
        assertEquals(employees, employeeCache.get(key));
    }

    @Test
    public void testGetNonExistentKey() {
        assertNull(employeeCache.get("nonExistentKey"));
    }

    @Test
    public void testContainsKey() {
        String key = "department2";
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee.Builder().id("3").name("Alice Johnson").salary(1500).build());

        assertFalse(employeeCache.containsKey(key));

        employeeCache.put(key, employees);

        assertTrue(employeeCache.containsKey(key));
    }
}
