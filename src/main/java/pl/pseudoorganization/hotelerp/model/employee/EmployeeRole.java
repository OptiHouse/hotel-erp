package pl.pseudoorganization.hotelerp.model.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmployeeRole {
    ACCOUNTANT("Accountant", "AC"),
    BARMAN("Barman", "BA"),
    CLEANER("Cleaner", "CL"),
    COOK("Cook", "CO"),
    MAINTENANCE("Maintenance", "MA"),
    MANAGER("Manager", "MN"),
    RECEPTIONIST("Receptionist", "RE"),
    SECURITY("Security", "SE"),
    SPA("Spa", "SP"),
    WAITER("Waiter", "WA");

    private final String name;
    private final String code;

    public static EmployeeRole createByCode(String code) {
        for (EmployeeRole role : EmployeeRole.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No EmployeeRole with code " + code + " found");
    }
}
