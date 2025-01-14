package com.javatalent.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javatalent.model.Employee;
import com.javatalent.repo.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> getAllEmployee() {
        return repository.findAll();
    }

    public Optional<Employee> findEmployeeById(long id) {
        return repository.findById(id);
    }
}
