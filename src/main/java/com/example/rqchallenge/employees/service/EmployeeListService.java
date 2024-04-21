package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.cache.EmployeeCache;
import com.example.rqchallenge.employees.client.DummyRequestClient;
import com.example.rqchallenge.employees.constant.EmployeeConstants;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.response.EmployeeResponse;
import com.example.rqchallenge.employees.response.EmployeeResponseList;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

import static com.example.rqchallenge.employees.constant.EmployeeConstants.SUCCESS;

@Service
public class EmployeeListService {
    @Autowired
    private DummyRequestClient dummyRequestClient;

    private EmployeeCache employeeCache;

    public EmployeeListService() {
        this.employeeCache = EmployeeCache.getInstance();
    }

    private final Logger logger = LoggerFactory.getLogger(EmployeeListService.class);

    @CircuitBreaker(name = EmployeeConstants.CIRCUIT_BREAKER_INSTANCE_RQ_CHALLENGE,
            fallbackMethod = EmployeeConstants.CIRCUIT_BREAKER_FALLBACK_GET_EMPLOYEES)
    public Map<String, List<Employee>> getAllEmployees() throws ServiceException {
        ResponseEntity<EmployeeResponseList> employeeResponseEntity = dummyRequestClient.getAllEmployees();
        handleErroneousResponses(employeeResponseEntity);
        EmployeeResponseList employeeResponseList = employeeResponseEntity.getBody();
        if (employeeResponseList == null || !employeeResponseList.getStatus().equals(SUCCESS)) {
            logger.error(EmployeeConstants.UNEXPECTED_RESPONSE_FORMAT);
            throw new ServiceException(EmployeeConstants.UNEXPECTED_RESPONSE_FORMAT);
        }
        return constructResponse(employeeResponseList.getData());
    }

    private Map<String, List<Employee>> getEmployeesFromCache(Throwable throwable) throws Throwable {
        if (throwable instanceof HttpClientErrorException httpClientErrorException) {
            if (httpClientErrorException.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                Employee employee = new Employee.Builder().name("sahil").salary(1000).age(29).id("1").
                        profileImageUrl("/profile.jpg").build();
                employeeCache.put("sahil", Collections.singletonList(employee));
                return employeeCache.getCache();
            }
        }
        logger.error("Error fetching response: " + throwable.getMessage());
        throw throwable;
    }

    private static Map<String, List<Employee>> constructResponse(List<Employee> employeeDataList) {
        Map<String, List<Employee>> employeesMapNameAsKey = new HashMap<>();
        for (Employee employee : employeeDataList) {
            String nameKey = employee.getName();
            employeesMapNameAsKey.computeIfAbsent(nameKey, k -> new ArrayList<>()).add(employee);
        }
        return employeesMapNameAsKey;
    }

    private void handleErroneousResponses(ResponseEntity<EmployeeResponseList> employeeResponseEntity)
            throws ServiceException {
        if (!(employeeResponseEntity.getStatusCode() == HttpStatus.OK)) {
            logger.error(EmployeeConstants.ERROR_WHILE_FETCHING_EMPLOYEES);
            throw new ServiceException(EmployeeConstants.ERROR_WHILE_FETCHING_EMPLOYEES);
        }
    }

    public List<Employee> search(String name) throws EmployeeNotFoundException {
        Map<String, List<Employee>> employeesMapWithNameAsKey;
        try {
            employeesMapWithNameAsKey = getAllEmployees();
        } catch (ServiceException e) {
            employeesMapWithNameAsKey = employeeCache.getCache();
        }
        if (!employeesMapWithNameAsKey.containsKey(name)) {
            logger.warn(EmployeeConstants.EMPLOYEE_NOT_FOUND);
            throw new EmployeeNotFoundException(EmployeeConstants.EMPLOYEE_NOT_FOUND);
        }
        return employeesMapWithNameAsKey.get(name);
    }

    @CircuitBreaker(name = EmployeeConstants.CIRCUIT_BREAKER_NAME_GET_EMPLOYEE_ID, fallbackMethod = "fallbackMethod")
    public Employee getEmployeeById(String employeeId) throws ServiceException, EmployeeNotFoundException {
        ResponseEntity<EmployeeResponse> employeeResponseEntity = dummyRequestClient.getEmployeeByID(employeeId);
        if (employeeResponseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found");
        }
        if (employeeResponseEntity.getStatusCode() != HttpStatus.OK) {
            logger.error("Unexpected status code: " + employeeResponseEntity.getStatusCodeValue());
            throw new ServiceException("Unexpected status code: " + employeeResponseEntity.getStatusCodeValue());
        }

        EmployeeResponse employeeResponse = employeeResponseEntity.getBody();
        if (employeeResponse == null || !employeeResponse.getStatus().equals(SUCCESS)) {
            logger.error(EmployeeConstants.UNEXPECTED_RESPONSE_FORMAT);
            throw new ServiceException(EmployeeConstants.UNEXPECTED_RESPONSE_FORMAT);
        }

        return employeeResponse.getData();
    }

    public Employee fallbackMethod(String employeeId, Throwable throwable) throws Throwable {
        if (throwable instanceof HttpClientErrorException httpClientErrorException) {
            if (httpClientErrorException.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                Optional<Employee> employee = employeeCache.getCache().values().stream()
                        .flatMap(List::stream).filter(emp -> emp.getId().equals(employeeId)).findFirst();
                if (employee.isPresent()) {
                    return employee.get();
                }
            }
        }
        logger.error("Error fetching response: " + throwable.getMessage());
        throw throwable;
    }
}
