package pl.pseudoorganization.hotelerp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.error.ErrorCode;
import pl.pseudoorganization.hotelerp.model.chore.Chore;
import pl.pseudoorganization.hotelerp.model.chore.ChoreRequest;
import pl.pseudoorganization.hotelerp.model.chore.ChoreStatus;
import pl.pseudoorganization.hotelerp.repository.ChoreRepository;
import pl.pseudoorganization.hotelerp.repository.EmployeeRepository;

import java.util.List;

@RestController
@RequestMapping("/api/chores")
@AllArgsConstructor
public class ChoreController {
    private final ChoreRepository choreRepository;
    private final EmployeeRepository employeeRepository;

    @PostMapping()
    public Chore createChore(@RequestBody ChoreRequest choreRequest) {
        Chore chore = Chore.from(choreRequest);
        choreRepository.save(chore);
        return chore;
    }

    @GetMapping()
    public List<Chore> getAllChores() {
        return choreRepository.findAll();
    }

    @GetMapping("/{id}")
    public Chore getChore(@PathVariable("id") String id) throws ApplicationException {
        var choreId = Long.parseLong(id);
        return choreRepository.findById(choreId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
    }

    @PatchMapping("/{id}/{action}")
    public Chore changeChoreStatus(@PathVariable("id") Long id,
                                   @PathVariable("action") String action) throws Exception {
        var choreStatus = getChoreStatus(action);
        var chore = choreRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        chore.changeStatus(choreStatus);
        choreRepository.save(chore);
        return chore;
    }

    @PatchMapping("/{id}/assign/{assignee}")
    public Chore assignToChore(@PathVariable("id") Long id,
                               @PathVariable("assignee") Long assignee) throws ApplicationException {
        var chore = choreRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        var employee = employeeRepository.findById(assignee).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        var updatedChore = chore.withAssignedTo(employee.getId());
        choreRepository.save(updatedChore);
        return updatedChore;
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
