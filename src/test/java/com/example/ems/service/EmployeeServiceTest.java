package com.example.ems.service;

import com.example.ems.exception.DuplicateEmployeeException;
import com.example.ems.exception.EmployeeNotFoundException;
import com.example.ems.exception.InvalidEmployeeException;
import com.example.ems.model.Employee;
import com.example.ems.repository.InMemoryEmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService service;

    @BeforeEach
    void setUp() {
        service = new EmployeeService(new InMemoryEmployeeRepository());
        service.addEmployee(101, "Alex", "Engineering", 90000, true);
        service.addEmployee(102, "Sam", "Engineering", 125000, true);
        service.addEmployee(103, "John", "Finance", 140000, false);
        service.addEmployee(104, "Priya", "Engineering", 150000, true);
    }

    @Test
    void activeEmployeesAbove100kAreSamAndPriya() {
        List<String> names = service.getActiveEarningMoreThan(100000).stream().map(Employee::getName).toList();
        assertEquals(List.of("Priya", "Sam"), names);
    }

    @Test
    void findByIdReturnsEmployee() {
        assertEquals("Sam", service.getById(102).getName());
    }

    @Test
    void missingEmployeeThrowsNotFound() {
        assertThrows(EmployeeNotFoundException.class, () -> service.getById(999));
    }

    @Test
    void duplicateIdIsRejected() {
        assertThrows(DuplicateEmployeeException.class, () -> service.addEmployee(101, "Zed", "HR", 1, true));
    }

    @Test
    void invalidInputIsRejected() {
        assertThrows(InvalidEmployeeException.class, () -> service.addEmployee(200, " ", "HR", 1, true));
        assertThrows(InvalidEmployeeException.class, () -> service.addEmployee(201, "Zed", "HR", -5, true));
    }

    @Test
    void departmentFilterIsCaseInsensitive() {
        assertEquals(3, service.getByDepartment("engineering").size());
    }
}
