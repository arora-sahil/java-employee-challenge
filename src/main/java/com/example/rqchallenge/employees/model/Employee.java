package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Employee {
    private String id;
    @JsonProperty("employee_name")
    private String name;
    @JsonProperty("employee_salary")
    private int salary;
    @JsonProperty("employee_age")
    private int age;
    @JsonProperty("profile_image")
    private String profileImageUrl;


    // Builder class
    public static class Builder {
        private final Employee employee;

        public Builder() {
            employee = new Employee();
        }

        public Builder id(String id) {
            employee.id = id;
            return this;
        }

        public Builder name(String name) {
            employee.name = name;
            return this;
        }

        public Builder salary(int salary) {
            employee.salary = salary;
            return this;
        }

        public Builder age(int age) {
            employee.age = age;
            return this;
        }

        public Builder profileImageUrl(String profileImageUrl) {
            employee.profileImageUrl = profileImageUrl;
            return this;
        }

        public Employee build() {
            return employee;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getAge() {
        return age;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return salary == employee.salary && age == employee.age && Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(profileImageUrl, employee.profileImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, age, profileImageUrl);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
