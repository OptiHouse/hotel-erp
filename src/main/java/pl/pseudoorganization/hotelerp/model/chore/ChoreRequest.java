package pl.pseudoorganization.hotelerp.model.chore;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoreRequest {
    private String title;
    private String description;
    private String type;
    private Long assignedTo;
    private Long createdBy;
}
