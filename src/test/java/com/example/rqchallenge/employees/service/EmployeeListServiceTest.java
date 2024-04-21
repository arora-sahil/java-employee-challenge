package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.cache.EmployeeCache;
import com.example.rqchallenge.employees.client.DummyRequestClient;
import com.example.rqchallenge.employees.constant.EmployeeConstants;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.response.EmployeeResponse;
import com.example.rqchallenge.employees.response.EmployeeResponseList;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeListServiceTest {

    @Mock
    private DummyRequestClient dummyRequestClient;

    @Mock
    private EmployeeCache employeeCache;

    @InjectMocks
    private EmployeeListService employeeListService;

    @BeforeEach
    void setUp() {
        CircuitBreakerRegistry.ofDefaults().getEventPublisher();
    }

    @Test
    void testGetAllEmployees_Success() throws ServiceException {
        EmployeeResponseList employeeResponseList = new EmployeeResponseList();
        employeeResponseList.setStatus("success");
        employeeResponseList.setData(Collections.singletonList(new Employee.Builder().name("sahil").age(10).build()));
        ResponseEntity<EmployeeResponseList> responseEntity = new ResponseEntity<>(employeeResponseList, HttpStatus.OK);
        when(dummyRequestClient.getAllEmployees()).thenReturn(responseEntity);

        Map<String, List<Employee>> result = employeeListService.getAllEmployees();
        assertNotNull(result);
    }

    @Test
    void testSearch_Success() throws ServiceException, EmployeeNotFoundException {
        List<Map<String, Object>> employeeDataList = new ArrayList<>();
        Map<String, Object> employeeData1 = new HashMap<>();
        employeeData1.put("id", "1");
        employeeData1.put("employee_name", "john");
        employeeData1.put("employee_salary", 50000);
        employeeData1.put("employee_age", 30);
        employeeData1.put("profile_image", "");

        Map<String, Object> employeeData2 = new HashMap<>();
        employeeData2.put("id", "2");
        employeeData2.put("employee_name", "Jane Smith");
        employeeData2.put("employee_salary", 60000);
        employeeData2.put("employee_age", 35);
        employeeData2.put("profile_image", "");

        employeeDataList.add(employeeData1);
        employeeDataList.add(employeeData2);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "success");
        responseBody.put("data", employeeDataList);
        EmployeeResponseList employeeResponseList = new EmployeeResponseList();
        employeeResponseList.setStatus("success");
        employeeResponseList.setData(Collections.singletonList(new Employee.Builder().name("john").age(10).build()));
        ResponseEntity<EmployeeResponseList> responseEntity = new ResponseEntity<>(employeeResponseList, HttpStatus.OK);
        when(dummyRequestClient.getAllEmployees()).thenReturn(responseEntity);
        List<Employee> result = employeeListService.search("john");
        assertNotNull(result);
    }

    @Test
    void testGetEmployeeById_Success() throws ServiceException, EmployeeNotFoundException {
        Employee expectedEmployee = new Employee.Builder().id("1").name("John").build();
        when(dummyRequestClient.getEmployeeByID(Mockito.anyString())).thenReturn(
                new ResponseEntity<>(new EmployeeResponse("success", expectedEmployee), HttpStatus.OK));
        Employee result = employeeListService.getEmployeeById("1");
        assertNotNull(result);
        assertEquals(expectedEmployee, result);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // Arrange
        String employeeId = "123";
        ResponseEntity<EmployeeResponse> employeeResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(dummyRequestClient.getEmployeeByID(employeeId)).thenReturn(employeeResponseEntity);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeListService.getEmployeeById(employeeId);
        });
    }

    @Test
    void testGetEmployeeById_UnexpectedStatusCode() {
        String employeeId = "123";
        ResponseEntity<EmployeeResponse> employeeResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(dummyRequestClient.getEmployeeByID(employeeId)).thenReturn(employeeResponseEntity);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            employeeListService.getEmployeeById(employeeId);
        });
        assertEquals("Unexpected status code: 500", exception.getMessage());
    }

    @Test
    void testGetEmployeeById_ResponseNotSuccess() {
        String employeeId = "123";
        EmployeeResponse employeeResponse = new EmployeeResponse("FAILURE", new Employee.Builder().build());
        ResponseEntity<EmployeeResponse> employeeResponseEntity = new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        when(dummyRequestClient.getEmployeeByID(employeeId)).thenReturn(employeeResponseEntity);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            employeeListService.getEmployeeById(employeeId);
        });
        assertEquals(EmployeeConstants.UNEXPECTED_RESPONSE_FORMAT, exception.getMessage());
    }
}
