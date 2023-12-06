package pl.pseudoorganization.hotelerp.model.employee;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    @Convert(converter = EmployeeRoleConverter.class)
    private EmployeeRole role;

    public Employee(String firstName, String lastName, String email, String password, EmployeeRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Employee(EmployeeRequest employeeRequest) {
        this.firstName = employeeRequest.getFirstName();
        this.lastName = employeeRequest.getLastName();
        this.email = employeeRequest.getEmail();
        this.password = employeeRequest.getPassword();
        this.role = EmployeeRole.createByCode(employeeRequest.getRole());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
