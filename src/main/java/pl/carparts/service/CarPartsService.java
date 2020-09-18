package pl.carparts.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.carparts.dao.model.PartAvailability;
import pl.carparts.dao.model.entities.Part;
import pl.carparts.dao.model.entities.SalesArgument;
import pl.carparts.dao.model.entities.ServiceAction;
import pl.carparts.dao.repositories.PartRepository;
import pl.carparts.dao.repositories.SalesArgumentRepository;
import pl.carparts.dao.repositories.ServiceActionRepository;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CarPartsService {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private PartRepository partRepository;
    private ServiceActionRepository serviceActionRepository;
    private SalesArgumentRepository salesArgumentRepository;

    public List<Part> findByBrandAndModel(String brand, String model) {
        return partRepository.findByBrandAndModel(brand, model);
    }

    public List<Part> findByBrandAndModelFilteringByNameAndDescription(String brand, String model, String name, String description) {
        if(!isNull(name) && !isNull(description)) {
            return partRepository.findByBrandAndModelFilteringByNameAndDescription(brand, model, name, description);
        } else if (!isNull(name)) {
            return partRepository.findByBrandAndModelFilteringByName(brand, model, name);
        } else if(!isNull(description)) {
            return partRepository.findByBrandAndModelFilteringByDescription(brand, model, description);
        } else {
            return partRepository.findByBrandAndModel(brand, model);
        }
    }

    public Map<String, List<Part>> findByBrand(String brand) {
        return partRepository.findByBrand(brand).stream().collect(groupingBy(Part::getModel, TreeMap::new, toList()));
    }

    public Optional<PartAvailability> checkAvailabilityAndShippingDate(Long id) {
        return partRepository.findById(id).map(part -> PartAvailability.builder()
                .availability(part.isAvailable())
                .shippingDate(part.isAvailable() ? DATE_FORMAT.format(DateUtils.addDays(new Date(), part.getShippingTime())) : "Not available")
                .build());
    }

    @Transactional
    public void updatePartDescription(Long id, String description) {
        partRepository.updatePartDescription(id, description);
    }

    @Transactional
    public Optional<ServiceAction> persistNewServiceAction(Long id, ServiceAction serviceAction) {
        Optional<Part> partById = partRepository.findById(id);
        if(partById.isPresent()) {
            ServiceAction newServiceAction = new ServiceAction(
                    serviceAction.getName(),
                    serviceAction.getActionStartDate(),
                    serviceAction.getActionFinishDate(),
                    partById.get());
            serviceActionRepository.save(newServiceAction);
            return Optional.of(newServiceAction);
        }
        return Optional.empty();
    }

    public List<ServiceAction> findAllActionsByDateRange(Date dateFrom, Date dateTo) {
        return serviceActionRepository.findAllActionsByDateRange(dateFrom, dateTo);
    }

    @Transactional
    public List<SalesArgument> deleteAllArgumentsByPartId(Long partId) {
        return salesArgumentRepository.deleteByPartId(partId);
    }
}
