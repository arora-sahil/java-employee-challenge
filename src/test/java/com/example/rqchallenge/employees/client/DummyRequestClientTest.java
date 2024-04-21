package com.example.rqchallenge.employees.client;

import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.response.EmployeeResponse;
import com.example.rqchallenge.employees.response.EmployeeResponseList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DummyRequestClientTest {

    @Mock
    private RestTemplate restTemplate;

    private DummyRequestClient dummyRequestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dummyRequestClient = new DummyRequestClient(restTemplate);
    }

    @Test
    public void testGetAllEmployees_Success() throws ServiceException {
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.getForEntity(any(String.class), any(Class.class))).thenReturn(mockResponseEntity);

        ResponseEntity<EmployeeResponseList> responseEntity = dummyRequestClient.getAllEmployees();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllEmployees_Failure() {
        when(restTemplate.getForEntity(any(String.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(HttpClientErrorException.class, () -> dummyRequestClient.getAllEmployees());
    }

    @Test
    public void testCreateEmployee_Success() throws ServiceException {
        // Mocking the response entity
        EmployeeResponse employeeResponse = new EmployeeResponse();
        ResponseEntity<EmployeeResponse> mockResponseEntity = new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(employeeResponse);

        // Creating an Employee object
        Employee employee = new Employee.Builder()
                .id("1")
                .name("John Doe")
                .salary(1000)
                .build();

        // Creating an HttpEntity for the employee object
        HttpEntity<Employee> entity = new HttpEntity<>(employee);

        // Invoking the createEmployee method
        EmployeeResponse response = dummyRequestClient.createEmployee(entity);

        // Asserting the response
        assertNotNull(response);
    }

    @Test
    public void testCreateEmployee_Failure() {
        // Mocking a failure scenario
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Employee employee = new Employee.Builder()
                .id("1")
                .name("John Doe")
                .salary(1000)
                .build();

        HttpEntity<Employee> entity = new HttpEntity<>(employee);

        assertThrows(HttpClientErrorException.class, () -> dummyRequestClient.createEmployee(entity));
    }

}
