package com.example.project.service;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    public String getAllEmployees() {
        return "List of employees";
    }
}
