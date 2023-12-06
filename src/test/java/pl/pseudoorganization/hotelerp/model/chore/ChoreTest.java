package pl.pseudoorganization.hotelerp.model.chore;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChoreTest {
    @Test
    void shouldChangeChoreStatusWhenAssigned() throws ChoreBusinessException {
        // given
        Chore chore = Chore.builder()
                .status(ChoreStatus.NEW)
                .assignedTo(1L)
                .build();

        // when
        chore.changeStatus(ChoreStatus.IN_PROGRESS);

        // then
        assertEquals(ChoreStatus.IN_PROGRESS, chore.getStatus());
    }

    @Test
    void shouldThrowChoreBusinessExceptionWhenUnassigned() {
        // given
        Chore chore = Chore.builder()
                .status(ChoreStatus.NEW)
                .build();

        // when, then
        assertThrows(ChoreBusinessException.class, () -> chore.changeStatus(ChoreStatus.DONE));
    }
}