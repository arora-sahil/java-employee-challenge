package com.example.rqchallenge.employees.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ServiceExceptionTest {

    @Test
    public void testConstructorAndGetMessage() {
        // Arrange
        String errorMessage = "Service error";

        // Act
        ServiceException exception = new ServiceException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
