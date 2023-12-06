package pl.pseudoorganization.hotelerp.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nullable;
import org.springframework.web.bind.annotation.*;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.error.ErrorCode;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRequest;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRole;
import pl.pseudoorganization.hotelerp.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping()
    public List<Employee> getAllEmployees(@RequestParam(required = false) @Nullable String lastName,
                                                   @RequestParam(required = false) @Nullable String role) throws ApplicationException {
        return getEmployeesByParam(lastName, role);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") String id) throws ApplicationException {
        return employeeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
    }

    @PostMapping()
    public Employee addEmployee(@RequestBody EmployeeRequest employeeRequest) throws ApplicationException {
        Optional<Employee> existingEmployee = employeeRepository.findByFirstNameAndLastName(employeeRequest.getFirstName(), employeeRequest.getLastName());
        if (existingEmployee.isPresent()) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
        }
        Employee employee = new Employee(employeeRequest);
        employeeRepository.save(employee);
        return employee;
    }

    private List<Employee> getEmployeesByParam(String lastName, String role) throws ApplicationException {
        if (StringUtils.isNotBlank(role)) {
            return employeeRepository.findByRole(EmployeeRole.createByCode(role))
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        }
        if (StringUtils.isNotBlank(lastName)) {
            return (employeeRepository.findByLastName(lastName))
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        }
        return employeeRepository.findAll();
    }
}
