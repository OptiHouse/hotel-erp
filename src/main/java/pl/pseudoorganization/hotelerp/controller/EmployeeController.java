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
    public ResponseEntity getAllEmployees(@RequestParam(required = false) String lastName,
                                          @RequestParam(required = false) String role) {
        try {
            return new ResponseEntity<>(getEmployeesByParam(lastName, role), HttpStatus.OK);
        } catch (ApplicationException e) {
            return handleApplicationException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getEmployeeById(@PathVariable("id") String id) {
        try {
            Employee employee = employeeRepository.findById(Long.parseLong(id))
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (ApplicationException e) {
            return handleApplicationException(e);
        }
    }

    @PostMapping()
    public ResponseEntity addEmployee(@RequestBody EmployeeRequest employee) {
        try {
            Optional<Employee> existingEmployee = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
            if (existingEmployee.isPresent()) {
                return new ResponseEntity<>(ErrorCode.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            employeeRepository.save(new Employee(employee));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorCode.OTHER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static ResponseEntity<ErrorCode> handleApplicationException(ApplicationException e) {
        if (e.getErrorCode() == ErrorCode.NOT_FOUND)
            return new ResponseEntity<>(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(ErrorCode.OTHER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<Employee> getEmployeesByParam(String lastName, String role) throws ApplicationException {
        if (StringUtils.isNotBlank(role)) {
            return employeeRepository.findByRole(EmployeeRole.createByCode(role))
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        }
        if (StringUtils.isNotBlank(lastName)) {
            return (employeeRepository.findByLastName(lastName))
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        } else {
            return employeeRepository.findAll();
        }
    }
}
