package pl.pseudoorganization.hotelerp.controller;

import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> getAllEmployees(@RequestParam(required = false) String lastName,
                                                   @RequestParam(required = false) String role) throws ApplicationException {
        return new ResponseEntity<>(getEmployeesByParam(lastName, role), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable("id") String id) throws ApplicationException {
        Employee employee = employeeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> addEmployee(@RequestBody EmployeeRequest employee) throws ApplicationException {
        Optional<Employee> existingEmployee = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
        if (existingEmployee.isPresent()) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
        }
        employeeRepository.save(new Employee(employee));
        return new ResponseEntity<>(HttpStatus.CREATED);
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
