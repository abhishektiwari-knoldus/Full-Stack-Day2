package com.example.ems.repository;

import com.example.ems.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

/** Stores employees in a Map keyed by ID: fast lookup, and results stay sorted by ID. */
@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<Integer, Employee> store = new ConcurrentSkipListMap<>();

    @Override
    public void save(Employee employee) {
        store.put(employee.getId(), employee);
    }

    @Override
    public Optional<Employee> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsById(int id) {
        return store.containsKey(id);
    }
}
