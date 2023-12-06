package pl.pseudoorganization.hotelerp.model.employee;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeTest {
    @Test
    void shouldChangeEmployeeRoleWhenPromoted() {
        // given
        Employee employee = Employee.builder()
                .role(EmployeeRole.BARMAN)
                .build();

        // when
        employee.changeRole(EmployeeRole.MANAGER);

        // then
        assertEquals(EmployeeRole.MANAGER, employee.getRole());
    }

    @Test
    void shouldThrowExceptionWhenWrongRoleProvided() {
        // given
        Employee employee = Employee.builder()
                .role(EmployeeRole.MANAGER)
                .build();

        // when, then
        assertThrows(IllegalArgumentException.class, () -> employee.changeRole(null));
    }
}