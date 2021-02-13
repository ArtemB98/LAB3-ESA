package com.app.controllers;

import com.app.entities.Department;
import com.app.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.app.repositories.EmployeeRepository;
import com.app.repositories.DepartmentRepository;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private Iterable<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @PostMapping(path = "/employee/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private Iterable<Employee> add(@RequestBody Employee newEmployee) {
        departmentRepository.save(newEmployee.getDepartmentByDepartmentId());
        employeeRepository.save(newEmployee);
        return employeeRepository.findAll();
    }

    @DeleteMapping(path = "employee/delete/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private Iterable<Employee> delete(@PathVariable String id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return employeeRepository.findAll();
        }
        employeeRepository.delete(employee);
        Department department = departmentRepository.findById(employee.getDepartmentByDepartmentId().getId()).orElse(null);
        if (employee != null) {
            try {
                departmentRepository.delete(department);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return employeeRepository.findAll();
    }
}
