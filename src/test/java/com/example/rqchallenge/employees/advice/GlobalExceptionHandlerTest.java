package com.example.rqchallenge.employees.advice;

import com.example.rqchallenge.employees.exception.EmployeeError;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.InvalidRequestException;
import com.example.rqchallenge.employees.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void testHandleServiceException() {
        String errorMessage = "Service exception message";
        ServiceException serviceException = new ServiceException(errorMessage);

        ResponseEntity<EmployeeError> responseEntity = globalExceptionHandler.handleException(serviceException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
    }

    @Test
    public void testHandleEmployeeNotFoundException() {
        String errorMessage = "Employee not found";
        EmployeeNotFoundException employeeNotFoundException = new EmployeeNotFoundException(errorMessage);

        ResponseEntity<EmployeeError> responseEntity = globalExceptionHandler.handleEmployeeNotFoundException(employeeNotFoundException);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
    }

    @Test
    public void testHandleInvalidRequestException() {
        String errorMessage = "Invalid request";
        InvalidRequestException invalidRequestException = new InvalidRequestException(errorMessage);

        ResponseEntity<EmployeeError> responseEntity = globalExceptionHandler.handleEmployeeNotFoundException(invalidRequestException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
    }
}
