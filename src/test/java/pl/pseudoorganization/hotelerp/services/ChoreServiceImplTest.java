package pl.pseudoorganization.hotelerp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.model.chore.Chore;
import pl.pseudoorganization.hotelerp.model.chore.ChoreRequest;
import pl.pseudoorganization.hotelerp.model.chore.ChoreStatus;
import pl.pseudoorganization.hotelerp.model.chore.ChoreType;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.repository.ChoreRepository;
import pl.pseudoorganization.hotelerp.repository.EmployeeRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChoreServiceImplTest {
    @Mock
    private ChoreRepository choreRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ChoreServiceImpl choreService;

    @Test
    void shouldCreateChore() throws ApplicationException {
        // given
        var employeeId = 1L;
        var employee = Employee.builder().id(employeeId).build();
        var choreRequest = ChoreRequest.builder()
                .title("title")
                .description("description")
                .type("CLEANING")
                .assignedTo(employeeId)
                .createdBy(employeeId)
                .build();

        // when
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        var response = choreService.createChore(choreRequest);

        // then
        assertEquals(choreRequest.getTitle(), response.getTitle());
        assertEquals(choreRequest.getDescription(), response.getDescription());
        assertEquals(choreRequest.getType(), response.getType().name());
        assertEquals(ChoreStatus.NEW, response.getStatus());
        assertEquals(choreRequest.getAssignedTo(), response.getAssignedTo());
        assertEquals(choreRequest.getCreatedBy(), response.getCreatedBy());
        verify(choreRepository).save(any());
    }

    @Test
    void shouldAssignEmployee() throws ApplicationException {
        // given
        var choreId = 1L;
        var employeeId = 1L;
        var choreRequest = ChoreRequest.builder()
                .title("title")
                .description("description")
                .type("CLEANING")
                .createdBy(1L)
                .build();
        var employee = Employee.builder().id(employeeId).build();

        // when
        when(choreRepository.findById(choreId)).thenReturn(Optional.of(Chore.from(choreRequest)));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        var response = choreService.assignToChore(choreId, employeeId);

        // then
        assertEquals(choreRequest.getTitle(), response.getTitle());
        assertEquals(choreRequest.getDescription(), response.getDescription());
        assertEquals(choreRequest.getType(), response.getType().name());
        assertEquals(ChoreStatus.NEW, response.getStatus());
        assertEquals(employeeId, response.getAssignedTo());
        assertEquals(choreRequest.getCreatedBy(), response.getCreatedBy());
        verify(choreRepository).save(any());
    }

    @ParameterizedTest
    @MethodSource("provideActions")
    void shouldChangeChoreStatus(final String action, final ChoreStatus expected) throws Exception {
        // given
        var choreId = 1L;
        var chore = Chore.builder()
                .id(choreId)
                .title("title")
                .description("description")
                .type(ChoreType.CLEANING)
                .assignedTo(1L)
                .createdBy(1L)
                .build();

        // when
        when(choreRepository.findById(choreId)).thenReturn(Optional.of(chore));
        var response = choreService.changeChoreStatus(choreId, action);

        // then
        assertEquals(chore.getTitle(), response.getTitle());
        assertEquals(chore.getDescription(), response.getDescription());
        assertEquals(chore.getType(), response.getType());
        assertEquals(expected, response.getStatus());
        assertEquals(chore.getAssignedTo(), response.getAssignedTo());
        assertEquals(chore.getCreatedBy(), response.getCreatedBy());
        verify(choreRepository).save(any());
    }

    private static Stream<Arguments> provideActions() {
        return Stream.of(
                Arguments.of("new", ChoreStatus.NEW),
                Arguments.of("start", ChoreStatus.IN_PROGRESS),
                Arguments.of("done", ChoreStatus.DONE),
                Arguments.of("cancel", ChoreStatus.CANCELED)
        );
    }

}