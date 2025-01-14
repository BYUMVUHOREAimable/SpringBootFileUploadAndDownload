package com.javatalent.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javatalent.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
