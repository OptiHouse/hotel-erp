package pl.pseudoorganization.hotelerp.services;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.error.ErrorCode;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRequest;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRole;
import pl.pseudoorganization.hotelerp.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees(@Nullable String lastName,
                                          @Nullable String role) throws ApplicationException {
        return getEmployeesByParam(lastName, role);
    }

    public Employee getEmployeeById(String id) throws ApplicationException {
        return employeeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
    }

    public Employee addEmployee(EmployeeRequest employeeRequest) throws ApplicationException {
        validateEmployeeFields(employeeRequest);
        checkForDupliactedEmployee(employeeRequest);
        Employee employee = new Employee(employeeRequest);
        employeeRepository.save(employee);
        return employee;
    }

    private static void validateEmployeeFields(EmployeeRequest employeeRequest) {
        if (employeeRequest.getFirstName() == null || employeeRequest.getFirstName() == null)
            throw new IllegalArgumentException("Wrong empolyee name");
    }

    private void checkForDupliactedEmployee(EmployeeRequest employeeRequest) throws ApplicationException {
        Optional<Employee> existingEmployee = employeeRepository.findByFirstNameAndLastName(employeeRequest.getFirstName(), employeeRequest.getLastName());
        if (existingEmployee.isPresent()) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
        }
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
