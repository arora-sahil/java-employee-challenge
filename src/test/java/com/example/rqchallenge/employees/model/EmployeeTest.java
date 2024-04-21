package com.example.rqchallenge.employees.model;

import com.example.rqchallenge.employees.model.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTest {

    @Test
    public void testBuilder() {
        String id = "123";
        String name = "John Doe";
        int salary = 5000;
        int age = 30;
        String profileImageUrl = "http://example.com/profile.jpg";

        Employee employee = new Employee.Builder()
                .id(id)
                .name(name)
                .salary(salary)
                .age(age)
                .profileImageUrl(profileImageUrl)
                .build();

        // Assert
        assertEquals(id, employee.getId());
        assertEquals(name, employee.getName());
        assertEquals(salary, employee.getSalary());
        assertEquals(age, employee.getAge());
        assertEquals(profileImageUrl, employee.getProfileImageUrl());
    }
}
