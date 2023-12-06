package pl.pseudoorganization.hotelerp.services;

import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRequest;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees(String lastName, String role) throws ApplicationException;

    Employee getEmployeeById(String id) throws ApplicationException;

    Employee addEmployee(EmployeeRequest employeeRequest) throws ApplicationException;

}
