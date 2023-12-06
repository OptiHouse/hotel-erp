package pl.pseudoorganization.hotelerp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.error.ErrorCode;
import pl.pseudoorganization.hotelerp.model.chore.Chore;
import pl.pseudoorganization.hotelerp.model.chore.ChoreRequest;
import pl.pseudoorganization.hotelerp.repository.ChoreRepository;
import pl.pseudoorganization.hotelerp.services.ChoreService;

import java.util.List;

@RestController
@RequestMapping("/api/chores")
@AllArgsConstructor
public class ChoreController {
    private final ChoreService choreService;
    private final ChoreRepository choreRepository;

    @PostMapping()
    public Chore createChore(@RequestBody ChoreRequest choreRequest) throws ApplicationException {
        return choreService.createChore(choreRequest);
    }

    @GetMapping()
    public List<Chore> getAllChores() {
        return choreRepository.findAll();
    }

    @GetMapping("/{id}")
    public Chore getChore(@PathVariable("id") Long id) throws ApplicationException {
        return choreRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
    }

    @PatchMapping("/{id}/{action}")
    public Chore changeChoreStatus(@PathVariable("id") Long id,
                                   @PathVariable("action") String action) throws Exception {
        return choreService.changeChoreStatus(id, action);
    }

    @PatchMapping("/{id}/assign/{assignee}")
    public Chore assignToChore(@PathVariable("id") Long id,
                               @PathVariable("assignee") Long assignee) throws ApplicationException {
        return choreService.assignToChore(id, assignee);
    }


}
