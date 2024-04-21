package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.cache.EmployeeCache;
import com.example.rqchallenge.employees.client.DummyRequestClient;
import com.example.rqchallenge.employees.constant.EmployeeConstants;
import com.example.rqchallenge.employees.exception.InvalidRequestException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.response.EmployeeResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmployeeCreateServiceTest {

    @Mock
    private DummyRequestClient dummyRequestClient;

    @Mock
    private EmployeeCache employeeCache;

    @InjectMocks
    private EmployeeCreateService employeeCreateService;

    @Test
    public void testCreate_Success() throws ServiceException, InvalidRequestException {
        Map<String, Object> employeeDetails = new HashMap<>();
        employeeDetails.put("name", "John Doe");
        employeeDetails.put("salary", 5000);
        employeeDetails.put("age", 30);
        Employee employee = new Employee.Builder()
                .name("John Doe")
                .salary(5000)
                .age(30)
                .build();
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setStatus("success");
        employeeResponse.setData(employee);

        when(dummyRequestClient.createEmployee(any())).thenReturn(employeeResponse);

        String status = employeeCreateService.create(employeeDetails);

        assertEquals("success", status);
    }

    @Test
    public void testCreate_InvalidRequestException() {
        Map<String, Object> employeeDetails = new HashMap<>();
        employeeDetails.put("name", "");
        employeeDetails.put("salary", 5000);
        employeeDetails.put("age", 30);

        assertThrows(InvalidRequestException.class, () -> {
            employeeCreateService.create(employeeDetails);
        });
    }

    @Test
    public void testCreate_ServiceException() {
        Map<String, Object> employeeDetails = new HashMap<>();
        employeeDetails.put(EmployeeConstants.NAME, "John Doe");
        employeeDetails.put(EmployeeConstants.SALARY, 5000);
        employeeDetails.put(EmployeeConstants.AGE, 30);

        when(dummyRequestClient.createEmployee(any())).
                thenThrow(new HttpClientErrorException(HttpStatus.CREATED));

        assertThrows(ServiceException.class, () -> {
            employeeCreateService.create(employeeDetails);
        });
    }

    @Test
    void testCreateEmployee_TooManyRequests() throws ServiceException, InvalidRequestException {
        Map<String, Object> employeeDetails = new HashMap<>();
        employeeDetails.put(EmployeeConstants.NAME, "John Doe");
        employeeDetails.put(EmployeeConstants.SALARY, 50000);
        employeeDetails.put(EmployeeConstants.AGE, 30);

        Employee employee = new Employee.Builder()
                .name("John Doe")
                .salary(50000)
                .age(30)
                .build();

        EmployeeResponse dummyResponse = new EmployeeResponse();
        dummyResponse.setStatus("dummy_success");
        dummyResponse.setData(employee);

        when(dummyRequestClient.createEmployee(any())).
                thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        String result = employeeCreateService.create(employeeDetails);

        assertEquals("dummy_success", result);
    }
}
