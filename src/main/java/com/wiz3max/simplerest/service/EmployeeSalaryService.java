package com.wiz3max.simplerest.service;

import com.wiz3max.simplerest.controller.model.request.EmployeeSalaryRequest;

import java.util.List;
import java.util.Map;

public interface EmployeeSalaryService {
    List<Map<String, Object>> getEmployeeSalary(EmployeeSalaryRequest request);
}
