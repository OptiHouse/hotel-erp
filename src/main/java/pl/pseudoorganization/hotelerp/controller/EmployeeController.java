package pl.pseudoorganization.hotelerp.controller;

import jakarta.annotation.Nullable;
import org.springframework.web.bind.annotation.*;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRequest;
import pl.pseudoorganization.hotelerp.services.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public List<Employee> getAllEmployees(@RequestParam(required = false) @Nullable String lastName,
                                                   @RequestParam(required = false) @Nullable String role) throws ApplicationException {
        return employeeService.getAllEmployees(lastName, role);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") String id) throws ApplicationException {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping()
    public Employee addEmployee(@RequestBody EmployeeRequest employeeRequest) throws ApplicationException {
        return employeeService.addEmployee(employeeRequest);
    }


}
