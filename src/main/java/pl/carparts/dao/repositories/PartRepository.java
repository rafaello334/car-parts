package pl.carparts.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.carparts.dao.model.Part;

import java.util.List;

public interface PartRepository extends CrudRepository<Part, Long> {

    List<Part> findByBrandAndModel(String brand, String model);
    List<Part> findByBrandAndModelAndNameContainsOrDescriptionContains(String brand, String model, String name, String description);
}
