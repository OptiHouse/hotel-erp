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
import pl.pseudoorganization.hotelerp.repository.ChoreRepository;

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
    private ChoreRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenValidInput_thenCreateChore() throws Exception {
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
    public void shouldGetChores() throws Exception {
        var chore = Chore.builder()
                .title("Test")
                .description("Test")
                .type(ChoreType.CLEANING)
                .status(ChoreStatus.NEW)
                .assignedTo(1L)
                .createdBy(1L)
                .build();
        var chores = List.of(chore);
        repository.save(chore);

        var response = mvc.perform(get("/api/chores"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var result = Arrays.asList(objectMapper.readValue(response, Chore[].class));

        assertThat(result).usingRecursiveComparison().isEqualTo(chores);
    }
}
