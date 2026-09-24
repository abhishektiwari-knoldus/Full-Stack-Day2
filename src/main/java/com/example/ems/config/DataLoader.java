package com.example.ems.config;

import com.example.ems.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seed(EmployeeService service) {
        return args -> {
            service.addEmployee(101, "Alex", "Engineering", 90000, true);
            service.addEmployee(102, "Sam", "Engineering", 125000, true);
            service.addEmployee(103, "John", "Finance", 140000, false);
            service.addEmployee(104, "Priya", "Engineering", 150000, true);

            System.out.println("Active employees with salary > 100000:");
            service.getAll().stream()
                    .filter(e -> e.isActive())
                    .filter(e -> e.getSalary() > 100000)
                    .forEach(System.out::println);
        };
    }
}
