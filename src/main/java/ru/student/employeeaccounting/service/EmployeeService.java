package ru.student.employeeaccounting.service;

import ru.student.employeeaccounting.model.Employee;
import ru.student.employeeaccounting.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> search(String name, String active) {
        String query = name == null ? "" : name.trim();
        boolean hasName = !query.isEmpty();

        if ("true".equals(active)) {
            return hasName
                    ? employeeRepository.findByFullNameContainingIgnoreCaseAndActive(query, true)
                    : employeeRepository.findByActive(true);
        }
        if ("false".equals(active)) {
            return hasName
                    ? employeeRepository.findByFullNameContainingIgnoreCaseAndActive(query, false)
                    : employeeRepository.findByActive(false);
        }
        return hasName
                ? employeeRepository.findByFullNameContainingIgnoreCase(query)
                : employeeRepository.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник с таким ID не найден"));
    }

    public boolean emailExists(String email) {
        return email != null && employeeRepository.existsByEmailIgnoreCase(email.trim());
    }

    public boolean emailExistsForAnotherEmployee(String email, Long id) {
        if (email == null) return false;
        return employeeRepository.findAll().stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(email.trim())
                        && (id == null || !e.getId().equals(id)));
    }

    public Employee save(Employee employee) {
        employee.setFullName(employee.getFullName().trim());
        employee.setEmail(employee.getEmail().trim());
        employee.setPhone(employee.getPhone().trim());
        employee.setPosition(employee.getPosition().trim());
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.delete(getById(id));
    }
}
