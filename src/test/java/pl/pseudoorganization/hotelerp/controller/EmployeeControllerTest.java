package pl.pseudoorganization.hotelerp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRequest;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRole;
import pl.pseudoorganization.hotelerp.services.EmployeeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void shouldCreateEmployee() throws ApplicationException {
        // given
        var employeeRequest = EmployeeRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .role(EmployeeRole.MANAGER.getCode())
                .build();
        var employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .role(EmployeeRole.MANAGER)
                .build();

        // when
        when(employeeService.addEmployee(employeeRequest)).thenReturn(employee);
        var response = employeeController.addEmployee(employeeRequest);

        // then
        assertEquals(employeeRequest.getFirstName(), response.getFirstName());
        assertEquals(employeeRequest.getLastName(), response.getLastName());
        assertEquals(EmployeeRole.MANAGER, response.getRole());
        verify(employeeService).addEmployee(any());
    }

    @Test
    void shouldGetEmployee() throws ApplicationException {
        // given
        var employeeId = 1L;
        var employee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .role(EmployeeRole.MANAGER)
                .build();

        // when
        when(employeeService.getEmployeeById(String.valueOf(employeeId))).thenReturn(employee);
        var response = employeeController.getEmployeeById(String.valueOf(employeeId));

        // then
        assertEquals(employee.getFirstName(), response.getFirstName());
        assertEquals(employee.getLastName(), response.getLastName());
        assertEquals(employee.getRole(), response.getRole());
    }

    @Test
    void shouldGetAllEmployees() throws ApplicationException {
        // given
        var employees = List.of(
                Employee.builder().id(1L).firstName("John").lastName("Doe").role(EmployeeRole.MANAGER).build(),
                Employee.builder().id(2L).firstName("Jane").lastName("Doe").role(EmployeeRole.ACCOUNTANT).build()
        );

        // when
        when(employeeService.getAllEmployees(null, null)).thenReturn(employees);
        var response = employeeController.getAllEmployees(null, null);

        // then
        assertEquals(employees.size(), response.size());
    }
}