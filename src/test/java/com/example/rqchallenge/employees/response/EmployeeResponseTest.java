package com.example.rqchallenge.employees.response;

import com.example.rqchallenge.employees.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
public class EmployeeResponseTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String status = "success";
        Employee employee = new Employee();
        EmployeeResponse employeeResponse = new EmployeeResponse();

        // Act
        employeeResponse.setStatus(status);
        employeeResponse.setData(employee);

        // Assert
        assertEquals(status, employeeResponse.getStatus());
        assertEquals(employee, employeeResponse.getData());
    }

    @Test
    public void testEmptyConstructor() {
        EmployeeResponse employeeResponse = new EmployeeResponse();

        // Assert
        assertNull(employeeResponse.getStatus());
        assertNull(employeeResponse.getData());
    }
}
