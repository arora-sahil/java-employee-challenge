package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.cache.EmployeeCache;
import com.example.rqchallenge.employees.client.DummyRequestClient;
import com.example.rqchallenge.employees.constant.EmployeeConstants;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmployeeDeleteService {
    @Autowired
    private DummyRequestClient dummyRequestClient;

    @Autowired
    private EmployeeListService employeeListService;

    private EmployeeCache employeeCache;

    public EmployeeDeleteService() {
        employeeCache = EmployeeCache.getInstance();
    }

    private final Logger logger = LoggerFactory.getLogger(EmployeeDeleteService.class);

    public String deleteEmployeeById(String employeeId) throws ServiceException, EmployeeNotFoundException {
        Employee employee = employeeListService.getEmployeeById(employeeId);
        if(employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found");
        }
        ResponseEntity<Object> deleteResponseEntity = dummyRequestClient.deleteEmployee(employeeId);

        if (deleteResponseEntity.getStatusCode() != HttpStatus.OK) {
            logger.error("Unexpected status code: " + deleteResponseEntity.getStatusCodeValue());
            throw new ServiceException("Failed to delete employee, unexpected status code: " + deleteResponseEntity.getStatusCodeValue());
        }

        Map<String, String> responseBody = (Map<String, String>) deleteResponseEntity.getBody();
        if (responseBody == null || !responseBody.containsKey("status") || !responseBody.get("status").equals("success")) {
            logger.error(EmployeeConstants.UNEXPECTED_RESPONSE_FORMAT);
            throw new ServiceException(EmployeeConstants.UNEXPECTED_RESPONSE_FORMAT);
        }
        employeeCache.remove(employee.getName());
        return employee.getName();
    }

}
