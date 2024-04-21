package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.cache.EmployeeCache;
import com.example.rqchallenge.employees.client.DummyRequestClient;
import com.example.rqchallenge.employees.constant.EmployeeConstants;
import com.example.rqchallenge.employees.exception.InvalidRequestException;
import com.example.rqchallenge.employees.exception.ServiceException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.response.EmployeeResponse;
import com.example.rqchallenge.util.RandomNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.rqchallenge.employees.constant.EmployeeConstants.DUMMY_SUCCESS;

@Service
public class EmployeeCreateService {
    private EmployeeCache employeeCache;

    @Autowired
    private DummyRequestClient dummyRequestClient;

    private final Logger logger = LoggerFactory.getLogger(EmployeeCreateService.class);

    public EmployeeCreateService() {
        this.employeeCache = EmployeeCache.getInstance();
    }

    public String create(Map<String, Object> employeeDetails) throws ServiceException, InvalidRequestException {
        Employee employee = buildEmployee(employeeDetails);
        if(employee.getName().isBlank()) {
            throw new InvalidRequestException(EmployeeConstants.EMPLOYEE_NAME_CAN_T_BE_NULL);
        }
        logger.info("Creating employee: {}", employee);

        HttpEntity<Employee> entity = new HttpEntity<>(employee);
        try {
            EmployeeResponse createdEmployee = dummyRequestClient.createEmployee(entity);
            if (createdEmployee == null || createdEmployee.getData() == null) {
                throw new ServiceException("Failed to create employee");
            }
            addOrUpdateCache(createdEmployee);
            return createdEmployee.getStatus();
        } catch (HttpClientErrorException exception) {
            if(exception.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                EmployeeResponse dummyResponse = new EmployeeResponse();
                employee.setId(String.valueOf(RandomNumberGenerator.generateRandomNumber(1000, 10000)));
                dummyResponse.setData(employee);
                addOrUpdateCache(dummyResponse);
                return DUMMY_SUCCESS;
            }
            logger.error("Failed to create employee: {}", exception.getMessage());
            throw new ServiceException(exception.getMessage());
        }
    }

    private void addOrUpdateCache(EmployeeResponse createdEmployee) {
        List<Employee> employeeList = employeeCache.get(createdEmployee.getData().getName());
        if (employeeList == null) {
            employeeList = new ArrayList<>();
            employeeList.add(createdEmployee.getData());
            employeeCache.put(createdEmployee.getData().getName(), employeeList);
        } else {
            employeeList.add(createdEmployee.getData());
        }
    }

    private static Employee buildEmployee(Map<String, Object> employeeDetails) {
        return new Employee.Builder()
                .name(employeeDetails.get(EmployeeConstants.NAME).toString())
                .salary(Integer.parseInt(employeeDetails.get(EmployeeConstants.SALARY).toString()))
                .age(Short.parseShort(employeeDetails.get(EmployeeConstants.AGE).toString()))
                .profileImageUrl(employeeDetails.get(EmployeeConstants.PROFILE_IMAGE).toString())
                .build();
    }
}
