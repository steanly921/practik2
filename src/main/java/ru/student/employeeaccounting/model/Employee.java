package ru.student.employeeaccounting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Введите ФИО сотрудника")
    @Size(min = 3, max = 120, message = "ФИО должно содержать от 3 до 120 символов")
    @Column(nullable = false, length = 120)
    private String fullName;

    @NotBlank(message = "Введите email")
    @Email(message = "Введите корректный email")
    @Size(max = 120, message = "Email не должен превышать 120 символов")
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank(message = "Введите телефон")
    @Pattern(regexp = "^(\\+7|8)?[\\s(\\-]*\\d[\\d\\s()\\-]{8,16}$",
            message = "Введите корректный номер телефона")
    @Column(nullable = false, length = 30)
    private String phone;

    @NotBlank(message = "Укажите должность")
    @Size(max = 80, message = "Должность не должна превышать 80 символов")
    @Column(nullable = false, length = 80)
    private String position;

    @NotNull(message = "Укажите зарплату")
    @DecimalMin(value = "0.0", inclusive = false, message = "Зарплата должна быть больше нуля")
    @Digits(integer = 10, fraction = 2, message = "Укажите зарплату числом, максимум 2 знака после запятой")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false)
    private boolean active = true;

    public Employee() {
    }

    public Employee(String fullName, String email, String phone, String position,
                    BigDecimal salary, boolean active) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
