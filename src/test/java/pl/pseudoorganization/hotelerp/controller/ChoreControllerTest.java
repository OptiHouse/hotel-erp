package pl.pseudoorganization.hotelerp.controller;

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
import pl.pseudoorganization.hotelerp.model.chore.ChoreType;
import pl.pseudoorganization.hotelerp.model.employee.Employee;
import pl.pseudoorganization.hotelerp.repository.ChoreRepository;
import pl.pseudoorganization.hotelerp.services.ChoreService;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChoreControllerTest {
    @Mock
    private ChoreRepository choreRepository;

    @Mock
    private ChoreService choreService;

    @InjectMocks
    private ChoreController choreController;

    @Test
    void shouldCreateChore() throws ApplicationException {
        // given
        var choreRequest = ChoreRequest.builder()
                .title("title")
                .description("description")
                .type("CLEANING")
                .assignedTo(1L)
                .createdBy(1L)
                .build();

        // when
        choreController.createChore(choreRequest);

        // then
        verify(choreService).createChore(eq(choreRequest));
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
        choreController.assignToChore(choreId, employeeId);

        // then
        verify(choreService).assignToChore(choreId, employeeId);
    }

    @ParameterizedTest
    @MethodSource("provideActions")
    void shouldChangeChoreStatus(final String action) throws Exception {
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
        choreController.changeChoreStatus(choreId, action);

        // then
        verify(choreService).changeChoreStatus(choreId, action);
    }

    private static Stream<Arguments> provideActions() {
        return Stream.of(
                Arguments.of("new"),
                Arguments.of("start"),
                Arguments.of("done"),
                Arguments.of("cancel")
        );
    }
}
