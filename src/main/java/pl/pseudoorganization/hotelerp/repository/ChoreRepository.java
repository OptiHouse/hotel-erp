package pl.pseudoorganization.hotelerp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pseudoorganization.hotelerp.model.chore.Chore;

public interface ChoreRepository extends JpaRepository<Chore, Long> {
}
