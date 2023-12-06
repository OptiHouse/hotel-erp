package pl.pseudoorganization.hotelerp.services;

import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.model.chore.Chore;
import pl.pseudoorganization.hotelerp.model.chore.ChoreRequest;

public interface ChoreService {
    Chore createChore(ChoreRequest choreRequest) throws ApplicationException;
    Chore assignToChore(Long id, Long assignee) throws ApplicationException;
    Chore changeChoreStatus(Long id, String action) throws Exception;
}
