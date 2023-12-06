package pl.pseudoorganization.hotelerp.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.error.ErrorCode;
import pl.pseudoorganization.hotelerp.model.chore.Chore;
import pl.pseudoorganization.hotelerp.model.chore.ChoreRequest;
import pl.pseudoorganization.hotelerp.model.chore.ChoreStatus;
import pl.pseudoorganization.hotelerp.repository.ChoreRepository;
import pl.pseudoorganization.hotelerp.repository.EmployeeRepository;

@Service
@AllArgsConstructor
public class ChoreServiceImpl implements ChoreService {

    private ChoreRepository choreRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public Chore createChore(ChoreRequest choreRequest) throws ApplicationException {
        if (choreRequest.getAssignedTo() != null) {
            employeeRepository.findById(choreRequest.getCreatedBy()).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        }
        employeeRepository.findById(choreRequest.getCreatedBy()).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));

        Chore chore = Chore.from(choreRequest);
        choreRepository.save(chore);
        return chore;
    }

    @Override
    public Chore assignToChore(Long id, Long assignee) throws ApplicationException {
        var chore = choreRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        var employee = employeeRepository.findById(assignee).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        var updatedChore = chore.withAssignedTo(employee.getId());
        choreRepository.save(updatedChore);
        return updatedChore;
    }

    @Override
    public Chore changeChoreStatus(Long id, String action) throws Exception {
        var choreStatus = getChoreStatus(action);
        var chore = choreRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        chore.changeStatus(choreStatus);
        choreRepository.save(chore);
        return chore;
    }

    private ChoreStatus getChoreStatus(String status) throws ApplicationException {
        return switch (status) {
            case "new" -> ChoreStatus.NEW;
            case "start" -> ChoreStatus.IN_PROGRESS;
            case "done" -> ChoreStatus.DONE;
            case "cancel" -> ChoreStatus.CANCELED;
            default -> throw new ApplicationException(ErrorCode.INVALID_STATUS);
        };
    }
}
