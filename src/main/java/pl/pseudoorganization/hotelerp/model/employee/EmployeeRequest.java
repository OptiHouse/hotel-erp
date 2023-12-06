package pl.pseudoorganization.hotelerp.model.employee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String role;
}
