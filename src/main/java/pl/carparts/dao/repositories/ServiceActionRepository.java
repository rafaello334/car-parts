package pl.carparts.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.carparts.dao.model.entities.ServiceAction;

import java.util.Date;
import java.util.List;

@Repository
public interface ServiceActionRepository extends CrudRepository<ServiceAction, Long> {
    @Query("select sa from ServiceAction sa where sa.actionStartDate >= ?1 and sa.actionFinishDate <= ?2")
    List<ServiceAction> findAllActionsByDateRange(Date dateFrom, Date dateTo);
}
