package com.example.rqchallenge.employees.response;

import com.example.rqchallenge.employees.model.Employee;

import java.util.List;

public class EmployeeResponseList {
    private String status;
    private List<Employee> data;

    public EmployeeResponseList() {
    }

    public EmployeeResponseList(String status, List<Employee> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }
}