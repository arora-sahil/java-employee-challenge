package com.example.rqchallenge.employees.cache;

import com.example.rqchallenge.employees.model.Employee;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmployeeCache {
    private static volatile EmployeeCache instance;
    private static final Object lock = new Object();
    private final Map<String, List<Employee>> cache = new HashMap<>();

    private EmployeeCache() {
    }

    // Public static method to get the singleton instance using double-checked locking
    public static EmployeeCache getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new EmployeeCache();
                }
            }
        }
        return instance;
    }

    public void put(String key, List<Employee> value) {
        cache.put(key, value);
    }

    public void remove(String key) {
        List<Employee> employees = cache.get(key);
        if(CollectionUtils.isEmpty(employees)) {
            return;
        }
        if (employees.size() <= 1) {
            cache.remove(key);
        } else {
            employees.removeIf(employee -> employee.getName().equals(key));
        }
    }

    public List<Employee> get(String key) {
        return cache.get(key);
    }

    public Map<String, List<Employee>> getCache() {
        return cache;
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }
}
