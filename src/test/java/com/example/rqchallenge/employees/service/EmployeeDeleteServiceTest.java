package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.client.DummyRequestClient;
import com.example.rqchallenge.employees.constant.EmployeeConstants;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeDeleteServiceTest {

    @Mock
    private DummyRequestClient dummyRequestClient;

    @Mock
    private EmployeeListService employeeListService;

    @InjectMocks
    private EmployeeDeleteService employeeDeleteService;

    private final String employeeId = "123";
    private final String employeeName = "John Doe";

    @Test
    void testDeleteEmployeeById_Success() throws ServiceException, EmployeeNotFoundException {
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName(employeeName);
        when(employeeListService.getEmployeeById(employeeId)).thenReturn(employee);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", "success");
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(dummyRequestClient.deleteEmployee(employeeId)).thenReturn(responseEntity);

        String deletedEmployeeName = employeeDeleteService.deleteEmployeeById(employeeId);

        assertEquals(employeeName, deletedEmployeeName);
        verify(dummyRequestClient, times(1)).deleteEmployee(employeeId);
    }

    @Test
    void testDeleteEmployeeById_EmployeeNotFound() throws ServiceException, EmployeeNotFoundException {
        when(employeeListService.getEmployeeById(anyString())).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeDeleteService.deleteEmployeeById(employeeId));
        verify(dummyRequestClient, never()).deleteEmployee(anyString());
    }

    @Test
    void testDeleteEmployeeById_UnexpectedStatusCode() {
        assertThrows(EmployeeNotFoundException.class, () -> employeeDeleteService.deleteEmployeeById(employeeId));
    }

    @Test
    void testDeleteEmployeeById_Failure() throws ServiceException, EmployeeNotFoundException {
        String employeeId = "123";
        Employee employee = new Employee();
        when(employeeListService.getEmployeeById(employeeId)).thenReturn(employee);
        ResponseEntity<Object> deleteResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(dummyRequestClient.deleteEmployee(employeeId)).thenReturn(deleteResponseEntity);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            employeeDeleteService.deleteEmployeeById(employeeId);
        });
        assertEquals("Failed to delete employee, unexpected status code: 500", exception.getMessage());
    }

    @Test
    void testDeleteEmployeeById_ResponseBodyMissingStatus() throws ServiceException, EmployeeNotFoundException {
        // Arrange
        String employeeId = "123";
        Employee employee = new Employee();
        employee.setName("John Doe");
        when(employeeListService.getEmployeeById(employeeId)).thenReturn(employee);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("otherKey", "otherValue");
        ResponseEntity<Object> deleteResponseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(dummyRequestClient.deleteEmployee(employeeId)).thenReturn(deleteResponseEntity);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            employeeDeleteService.deleteEmployeeById(employeeId);
        });
        assertEquals(EmployeeConstants.UNEXPECTED_RESPONSE_FORMAT, exception.getMessage());
    }

}
