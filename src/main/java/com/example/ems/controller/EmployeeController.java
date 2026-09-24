package com.example.ems.controller;

import com.example.ems.exception.EmployeeException;
import com.example.ems.model.Employee;
import com.example.ems.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String showAll(Model model) {
        return render(model, "All employees", service.getAll(), null);
    }

    @PostMapping("/employees")
    public String add(@RequestParam String id,
                      @RequestParam String name,
                      @RequestParam String department,
                      @RequestParam String salary,
                      @RequestParam(defaultValue = "false") boolean active,
                      RedirectAttributes redirect) {
        try {
            Employee e = service.addEmployee(Integer.parseInt(id.trim()), name, department,
                    Double.parseDouble(salary.trim()), active);
            redirect.addFlashAttribute("message", "Added " + e.getName() + " (ID " + e.getId() + ").");
        } catch (NumberFormatException ex) {
            redirect.addFlashAttribute("error", "ID must be a whole number and salary must be a number.");
        } catch (EmployeeException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/search/id")
    public String searchById(@RequestParam String id, Model model) {
        try {
            Employee e = service.getById(Integer.parseInt(id.trim()));
            return render(model, "Employee " + e.getId(), List.of(e), null);
        } catch (NumberFormatException ex) {
            return render(model, "Search by ID", List.of(), "Enter the ID as a whole number, for example 101.");
        } catch (EmployeeException ex) {
            return render(model, "Search by ID", List.of(), ex.getMessage());
        }
    }

    @GetMapping("/search/department")
    public String searchByDepartment(@RequestParam String department, Model model) {
        List<Employee> result = service.getByDepartment(department);
        String error = result.isEmpty() ? "No employees found in \"" + department.trim() + "\"." : null;
        return render(model, "Department: " + department.trim(), result, error);
    }

    @GetMapping("/search/active-above")
    public String searchActiveAbove(@RequestParam String minSalary, Model model) {
        try {
            double min = Double.parseDouble(minSalary.trim());
            List<Employee> result = service.getActiveEarningMoreThan(min);
            String error = result.isEmpty() ? "No active employees earn more than " + minSalary.trim() + "." : null;
            return render(model, "Active employees earning more than " + minSalary.trim(), result, error);
        } catch (NumberFormatException ex) {
            return render(model, "Active employees by salary", List.of(), "Enter the minimum salary as a number.");
        }
    }

    private String render(Model model, String heading, List<Employee> employees, String error) {
        model.addAttribute("heading", heading);
        model.addAttribute("employees", employees);
        model.addAttribute("departments", service.getDepartments());
        model.addAttribute("total", service.getAll().size());
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "index";
    }
}
