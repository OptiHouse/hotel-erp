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
import pl.pseudoorganization.hotelerp.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    @Mock
    private EmployeeRepository employeeRepository;

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

        // when
        var response = employeeController.addEmployee(employeeRequest);

        // then
        assertEquals(employeeRequest.getFirstName(), response.getFirstName());
        assertEquals(employeeRequest.getLastName(), response.getLastName());
        assertEquals(EmployeeRole.MANAGER, response.getRole());
        verify(employeeRepository).save(any());
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
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
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
        when(employeeRepository.findAll()).thenReturn(employees);
        var response = employeeController.getAllEmployees(null, null);

        // then
        assertEquals(employees.size(), response.size());
    }
}