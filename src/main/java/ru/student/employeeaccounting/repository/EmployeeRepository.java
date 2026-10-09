package ru.student.employeeaccounting.repository;

import ru.student.employeeaccounting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmailIgnoreCase(String email);
    List<Employee> findByFullNameContainingIgnoreCase(String fullName);
    List<Employee> findByActive(boolean active);
    List<Employee> findByFullNameContainingIgnoreCaseAndActive(String fullName, boolean active);
}
