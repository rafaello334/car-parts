package pl.carparts.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.carparts.dao.model.entities.SalesArgument;

import java.util.List;

@Repository
public interface SalesArgumentRepository extends CrudRepository<SalesArgument, Long> {
    List<SalesArgument> deleteByPartId(Long partId);
}
