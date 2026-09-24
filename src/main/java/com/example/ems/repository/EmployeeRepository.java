package com.example.ems.repository;

import com.example.ems.model.Employee;

import java.util.List;
import java.util.Optional;

/** Abstraction over storage. The service depends on this interface, not on a concrete class. */
public interface EmployeeRepository {
    void save(Employee employee);

    Optional<Employee> findById(int id);

    List<Employee> findAll();

    boolean existsById(int id);
}
