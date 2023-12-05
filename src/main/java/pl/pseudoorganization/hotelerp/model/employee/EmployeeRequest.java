package pl.pseudoorganization.hotelerp.model.employee;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String role;
}
