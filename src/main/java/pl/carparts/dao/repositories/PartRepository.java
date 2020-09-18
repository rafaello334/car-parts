package pl.carparts.dao.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.carparts.dao.model.entities.Part;

import java.util.List;

@Repository
public interface PartRepository extends CrudRepository<Part, Long> {

    List<Part> findByBrand(String brand);

    List<Part> findByBrandAndModel(String brand, String model);

    @Query("select p from Part p where p.brand = ?1 and p.model = ?2 and p.name like %?3%")
    List<Part> findByBrandAndModelFilteringByName(String brand, String model, String name);

    @Query("select p from Part p where p.brand = ?1 and p.model = ?2 and p.description like %?3%")
    List<Part> findByBrandAndModelFilteringByDescription(String brand, String model, String description);

    @Query("select p from Part p where p.brand = ?1 and p.model = ?2 and p.name like %?3% or p.description like %?4%")
    List<Part> findByBrandAndModelFilteringByNameAndDescription(String brand, String model, String name, String description);

    @Modifying
    @Query("update Part p set p.description = ?2 where p.id = ?1")
    void updatePartDescription(Long id, String description);
}
