package ru.student.employeeaccounting.controller;

import jakarta.validation.Valid;
import ru.student.employeeaccounting.model.Employee;
import ru.student.employeeaccounting.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String list(@RequestParam(required = false, defaultValue = "") String name,
                       @RequestParam(required = false, defaultValue = "all") String active,
                       Model model) {
        if (!active.equals("all") && !active.equals("true") && !active.equals("false")) {
            active = "all";
        }
        model.addAttribute("employees", employeeService.search(name, active));
        model.addAttribute("name", name);
        model.addAttribute("active", active);
        return "employees-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("pageTitle", "Добавить сотрудника");
        model.addAttribute("formAction", "/employees/add");
        return "employee-form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("employee") Employee employee,
                      BindingResult result, Model model,
                      RedirectAttributes redirectAttributes) {
        if (employeeService.emailExists(employee.getEmail())) {
            result.rejectValue("email", "duplicate", "Сотрудник с таким email уже существует");
        }
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Добавить сотрудника");
            model.addAttribute("formAction", "/employees/add");
            return "employee-form";
        }
        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Сотрудник успешно добавлен");
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("employee", employeeService.getById(id));
            model.addAttribute("pageTitle", "Редактировать сотрудника");
            model.addAttribute("formAction", "/employees/edit/" + id);
            return "employee-form";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/employees";
        }
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("employee") Employee employee,
                       BindingResult result, Model model,
                       RedirectAttributes redirectAttributes) {
        try {
            Employee existing = employeeService.getById(id);
            employee.setId(id);
            if (employeeService.emailExistsForAnotherEmployee(employee.getEmail(), id)) {
                result.rejectValue("email", "duplicate", "Сотрудник с таким email уже существует");
            }
            if (result.hasErrors()) {
                model.addAttribute("pageTitle", "Редактировать сотрудника");
                model.addAttribute("formAction", "/employees/edit/" + id);
                return "employee-form";
            }
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Данные сотрудника обновлены");
            return "redirect:/employees";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/employees";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Сотрудник удалён");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employees";
    }
}
