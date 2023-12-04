package pl.pseudoorganization.hotelerp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRole;
import pl.pseudoorganization.hotelerp.repository.EmployeeRepository;

@SpringBootApplication
public class HotelErpApplication {
    @Autowired
    EmployeeRepository employeeRepository; // temporary solution for testing purpose

    public static void main(String[] args) {
        SpringApplication.run(HotelErpApplication.class, args);
    }

    // temporary solution for testing purpose
    @Bean
    public CommandLineRunner demo(EmployeeRepository repository) {
        return args -> {
            repository.save(new Employee("Jan", "Kowalski", "jan.kowalski@example.com", "password", EmployeeRole.ACCOUNTANT));
            repository.save(new Employee("Adam", "Nowak", "adam.nowak@example.com", "password", EmployeeRole.MANAGER));
        };
    }
}
