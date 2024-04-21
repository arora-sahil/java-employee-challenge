package com.example.rqchallenge.employees.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmployeeNotFoundExceptionTest {

    @Test
    public void testConstructorAndGetMessage() {
        // Arrange
        String errorMessage = "Employee not found";

        // Act
        EmployeeNotFoundException exception = new EmployeeNotFoundException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
