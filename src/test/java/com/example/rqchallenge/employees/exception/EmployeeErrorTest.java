package com.example.rqchallenge.employees.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmployeeErrorTest {

    @Test
    public void testGetStatus() {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        EmployeeError employeeError = new EmployeeError();
        employeeError.setStatus(httpStatus);

        int status = employeeError.getStatus();

        assertEquals(httpStatus.value(), status);
    }

    @Test
    public void testSetStatus() {
        int statusCode = HttpStatus.NOT_FOUND.value();
        EmployeeError employeeError = new EmployeeError();

        employeeError.setStatus(HttpStatus.NOT_FOUND);

        assertEquals(statusCode, employeeError.getStatus());
    }

    @Test
    public void testGetMessage() {
        String message = "Employee not found";
        EmployeeError employeeError = new EmployeeError();
        employeeError.setMessage(message);

        String errorMessage = employeeError.getMessage();

        assertEquals(message, errorMessage);
    }

    @Test
    public void testSetMessage() {
        String message = "Internal server error";
        EmployeeError employeeError = new EmployeeError();
        employeeError.setMessage(message);

        assertEquals(message, employeeError.getMessage());
    }
}
