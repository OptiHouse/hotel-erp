package pl.pseudoorganization.hotelerp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRole;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<List<Employee>> findByLastName(String lastName);

    Optional<List<Employee>> findByRole(EmployeeRole role);

    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);
}
