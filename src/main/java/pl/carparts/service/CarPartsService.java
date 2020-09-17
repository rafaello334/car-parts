package pl.carparts.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.carparts.dao.model.Part;
import pl.carparts.dao.repositories.PartRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CarPartsService {

    private PartRepository partRepository;

    public List<Part> findByBrandAndModel(String brand, String model) {
        return partRepository.findByBrandAndModel(brand, model);
    }

    public List<Part> findByBrandAndModelAndNameContainsOrDescriptionContains(String brand, String model, String name, String description) {
        return partRepository.findByBrandAndModelAndNameContainsOrDescriptionContains(brand, model, name, description);
    }
}
