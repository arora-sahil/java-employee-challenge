package com.example.rqchallenge.employees.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmployeeConfigurationTest {

    @Test
    public void testRestTemplateBean() {
        EmployeeConfiguration employeeConfiguration = new EmployeeConfiguration();

        RestTemplate restTemplate = employeeConfiguration.restTemplate();

        assertNotNull(restTemplate);
    }
}
