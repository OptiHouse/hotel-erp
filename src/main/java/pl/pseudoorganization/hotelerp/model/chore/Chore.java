package pl.pseudoorganization.hotelerp.model.chore;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Chore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private ChoreType type;

    @Column
    @Enumerated(EnumType.STRING)
    private ChoreStatus status;

    @With
    @Column
    private Long assignedTo;

    @Column
    private Long createdBy;

    Chore(ChoreRequest choreRequest) {
        this.title = choreRequest.getTitle();
        this.description = choreRequest.getDescription();
        this.type = ChoreType.valueOf(choreRequest.getType());
        this.status = ChoreStatus.NEW;
        this.assignedTo = choreRequest.getAssignedTo();
        this.createdBy = choreRequest.getCreatedBy();
    }

    public static Chore from(final ChoreRequest choreRequest) {
        return new Chore(choreRequest);
    }

    public void changeStatus(final ChoreStatus status) throws ChoreBusinessException {
        if (assignedTo == null) {
            throw new ChoreBusinessException("Cannot change status of unassigned chore");
        }

        this.status = status;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Chore chore = (Chore) o;
        return getId() != null && Objects.equals(getId(), chore.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
