package com.example.rqchallenge.employees.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvalidRequestExceptionTest {

    @Test
    public void testConstructorAndGetMessage() {
        // Arrange
        String errorMessage = "Invalid request";

        // Act
        InvalidRequestException exception = new InvalidRequestException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
