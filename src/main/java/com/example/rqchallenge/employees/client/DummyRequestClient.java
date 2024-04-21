package com.example.rqchallenge.employees.client;

import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.response.EmployeeResponse;
import com.example.rqchallenge.employees.response.EmployeeResponseList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class DummyRequestClient {

    @Value("${create.employee.path}")
    private String createEmployeePath;

    @Value("${employees.portal}")
    private String url;

    @Value("${all.employee.path}")
    private String allEmployeePath;

    @Value("${get.employee.path}")
    private String getEmployeePath;

    private RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(DummyRequestClient.class);


    public DummyRequestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmployeeResponse createEmployee(HttpEntity<Employee> entity) {
        return restTemplate.postForObject(
                url + createEmployeePath,
                entity,
                EmployeeResponse.class
        );
    }

    public ResponseEntity<Object> deleteEmployee(String employeeId) throws ServiceException {
        ResponseEntity<Object> deleteResponseEntity;
        try {
            deleteResponseEntity = restTemplate.exchange(url + "/" + employeeId, HttpMethod.DELETE,
                    null, Object.class);
        } catch (HttpClientErrorException exception) {
            logger.error("Error deleting employee: " + exception.getMessage());
            throw new ServiceException("Failed to delete employee: " + exception.getMessage());
        }
        return deleteResponseEntity;
    }

    public ResponseEntity<EmployeeResponseList> getAllEmployees() throws ServiceException {
        ResponseEntity<EmployeeResponseList> employeeResponseEntity;
        employeeResponseEntity = restTemplate.getForEntity(url + allEmployeePath, EmployeeResponseList.class);
        return employeeResponseEntity;
    }

    public ResponseEntity<EmployeeResponse> getEmployeeByID(String employeeId) {
        ResponseEntity<EmployeeResponse> employeeResponseEntity;
        employeeResponseEntity = restTemplate.getForEntity(url + getEmployeePath + employeeId,
                EmployeeResponse.class);
        return employeeResponseEntity;
    }
}
