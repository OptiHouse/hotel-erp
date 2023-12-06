package pl.pseudoorganization.hotelerp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.pseudoorganization.hotelerp.model.chore.Chore;
import pl.pseudoorganization.hotelerp.model.chore.ChoreRequest;
import pl.pseudoorganization.hotelerp.model.chore.ChoreStatus;
import pl.pseudoorganization.hotelerp.model.chore.ChoreType;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRequest;
import pl.pseudoorganization.hotelerp.model.employee.EmployeeRole;
import pl.pseudoorganization.hotelerp.repository.ChoreRepository;
import pl.pseudoorganization.hotelerp.repository.EmployeeRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class HotelErpApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ChoreRepository choreRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenValidInput_thenCreateChore() throws Exception {
        var choreRequest = ChoreRequest.builder()
                .title("Test")
                .description("Test")
                .type("CLEANING")
                .assignedTo(1L)
                .createdBy(1L)
                .build();

        var response = mvc.perform(post("/api/chores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(choreRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var result = objectMapper.readValue(response, Chore.class);

        assertEquals(choreRequest.getTitle(), result.getTitle());
        assertEquals(choreRequest.getDescription(), result.getDescription());
        assertEquals(ChoreStatus.NEW, result.getStatus());
        assertEquals(choreRequest.getType(), result.getType().name());
        assertEquals(choreRequest.getAssignedTo(), result.getAssignedTo());
        assertEquals(choreRequest.getCreatedBy(), result.getCreatedBy());
    }

    @Test
    void shouldGetChores() throws Exception {
        var chore = Chore.builder()
                .title("Test")
                .description("Test")
                .type(ChoreType.CLEANING)
                .status(ChoreStatus.NEW)
                .assignedTo(1L)
                .createdBy(1L)
                .build();
        var chores = List.of(chore);
        choreRepository.save(chore);

        var response = mvc.perform(get("/api/chores"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var result = Arrays.asList(objectMapper.readValue(response, Chore[].class));

        assertThat(result).usingRecursiveComparison().isEqualTo(chores);
    }

    @Test
    void whenValidInput_thenCreateEmployee() throws Exception {
        var employeeRequest = EmployeeRequest.builder()
                .firstName("Artur")
                .lastName("Kowalski")
                .email("Artur.kowalski@example.com")
                .password("password")
                .role(EmployeeRole.MANAGER.getCode())
                .build();

        var response = mvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var result = objectMapper.readValue(response, Employee.class);

        assertEquals(employeeRequest.getFirstName(), result.getFirstName());
        assertEquals(employeeRequest.getLastName(), result.getLastName());
        assertEquals(employeeRequest.getEmail(), result.getEmail());
        assertEquals(employeeRequest.getRole(), result.getRole().getCode());
    }

    @Test
    void shouldGetEmployees() throws Exception {
        var employee = Employee.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan.kowalski@example.com")
                .password("password")
                .role(EmployeeRole.MANAGER)
                .build();
        var employees = List.of(employee);
        employeeRepository.save(employee);

        var response = mvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var result = Arrays.asList(objectMapper.readValue(response, Employee[].class));

        assertThat(result).usingRecursiveComparison().isEqualTo(employees);
    }
}
