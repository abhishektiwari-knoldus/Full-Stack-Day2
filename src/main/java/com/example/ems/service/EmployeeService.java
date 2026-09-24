package com.example.ems.service;

import com.example.ems.exception.DuplicateEmployeeException;
import com.example.ems.exception.EmployeeNotFoundException;
import com.example.ems.exception.InvalidEmployeeException;
import com.example.ems.model.Employee;
import com.example.ems.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee addEmployee(int id, String name, String department, double salary, boolean active) {
        if (id <= 0) {
            throw new InvalidEmployeeException("Employee ID must be a positive number.");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidEmployeeException("Name is required.");
        }
        if (department == null || department.isBlank()) {
            throw new InvalidEmployeeException("Department is required.");
        }
        if (salary < 0) {
            throw new InvalidEmployeeException("Salary cannot be negative.");
        }
        if (repository.existsById(id)) {
            throw new DuplicateEmployeeException(id);
        }
        Employee employee = new Employee(id, name.trim(), department.trim(), salary, active);
        repository.save(employee);
        return employee;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    /** Optional.orElseThrow turns "missing" into our custom exception. */
    public Employee getById(int id) {
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public List<Employee> getByDepartment(String department) {
        String wanted = department == null ? "" : department.trim();
        return repository.findAll().stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(wanted))
                .collect(Collectors.toList());
    }

    public List<Employee> getActiveEarningMoreThan(double minSalary) {
        return repository.findAll().stream()
                .filter(Employee::isActive)
                .filter(e -> e.getSalary() > minSalary)
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .collect(Collectors.toList());
    }

    /** A Set gives each department once; TreeSet keeps them alphabetical. */
    public Set<String> getDepartments() {
        return repository.findAll().stream()
                .map(Employee::getDepartment)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
