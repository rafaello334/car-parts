package pl.carparts.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.carparts.dao.model.PartAvailability;
import pl.carparts.dao.model.entities.Part;
import pl.carparts.dao.model.entities.SalesArgument;
import pl.carparts.dao.model.entities.ServiceAction;
import pl.carparts.dao.repositories.PartRepository;
import pl.carparts.dao.repositories.SalesArgumentRepository;
import pl.carparts.dao.repositories.ServiceActionRepository;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarPartsServiceTest {

    private CarPartsService carPartsService;

    @Mock
    private PartRepository partRepository;
    @Mock
    private ServiceActionRepository serviceActionRepository;
    @Mock
    private SalesArgumentRepository salesArgumentRepository;

    @BeforeEach
    void setUpTests() {
        carPartsService = new CarPartsService(partRepository, serviceActionRepository, salesArgumentRepository);
    }

    @Test
    void shouldReturnListOfPartsByBranchAndModel() {
        List<Part> methodResult = Collections.singletonList(new Part());
        when(partRepository.findByBrandAndModel("Audi", "A4")).thenReturn(methodResult);
        List<Part> result = carPartsService.findByBrandAndModel("Audi", "A4");
        Assert.assertEquals(methodResult, result);
    }

    @Test
    void shouldReturnListOfPartsByBranchAndModelWithNoFilteringIfNameAndDescriptionAreNull() {
        List<Part> methodResult = Collections.singletonList(new Part());
        when(partRepository.findByBrandAndModel("Audi", "A4")).thenReturn(methodResult);
        List<Part> result = carPartsService.findByBrandAndModelFilteringByNameAndDescription("Audi", "A4", null, null);
        Assert.assertEquals(methodResult, result);
    }

    @Test
    void shouldReturnListOfPartsByBranchAndModelFilteringByName() {
        List<Part> methodResult = Collections.singletonList(new Part());
        when(partRepository.findByBrandAndModelFilteringByName("Audi", "A4", "Name")).thenReturn(methodResult);
        List<Part> result = carPartsService.findByBrandAndModelFilteringByNameAndDescription("Audi", "A4", "Name", null);
        Assert.assertEquals(methodResult, result);
    }

    @Test
    void shouldReturnListOfPartsByBranchAndModelFilteringByDescription() {
        List<Part> methodResult = Collections.singletonList(new Part());
        when(partRepository.findByBrandAndModelFilteringByDescription("Audi", "A4", "Desc")).thenReturn(methodResult);
        List<Part> result = carPartsService.findByBrandAndModelFilteringByNameAndDescription("Audi", "A4", null, "Desc");
        Assert.assertEquals(methodResult, result);
    }

    @Test
    void shouldReturnListOfPartsByBranchAndModelFilteringByNameAndDescription() {
        List<Part> methodResult = Collections.singletonList(new Part());
        when(partRepository.findByBrandAndModelFilteringByNameAndDescription("Audi", "A4", "Name", "Desc")).thenReturn(methodResult);
        List<Part> result = carPartsService.findByBrandAndModelFilteringByNameAndDescription("Audi", "A4", "Name", "Desc");
        Assert.assertEquals(methodResult, result);
    }

    @Test
    void shouldReturnMapWithModelAsKeyAndItsPartList() {
        Part part1 = new Part("Audi", "A1", null, null, "Name1", "Desc1", 1.0, true, 1, null, null);
        Part part2 = new Part("Audi", "A3", null, null, "Name2", "Desc2", 1.0, true, 1, null, null);
        Part part3 = new Part("Audi", "A3", null, null, "Name3", "Desc3", 1.0, true, 1, null, null);

        Map<String, List<Part>> expectedResult = new TreeMap<>();
        expectedResult.put("A1", Collections.singletonList(part1));
        expectedResult.put("A3", Arrays.asList(part2, part3));

        List<Part> databaseResult = Arrays.asList(part1, part2, part3);
        when(partRepository.findByBrand("Audi")).thenReturn(databaseResult);

        Map<String, List<Part>> result = carPartsService.findByBrand("Audi");
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    void shouldReturnAvailabilityAndShippingDateForAvailableRecord() {
        Part part1 = new Part("Audi", "A1", null, null, "Name1", "Desc1", 1.0, true, 1, null, null);

        Optional<PartAvailability> expectedResult = Optional.of(new PartAvailability(true, "2020-09-19"));

        when(partRepository.findById(1L)).thenReturn(Optional.of(part1));

        Optional<PartAvailability> result = carPartsService.checkAvailabilityAndShippingDate(1L);
        assertThat(result, samePropertyValuesAs(expectedResult));
    }

    @Test
    void shouldReturnNotAvailableShippingDateForNotAvailableRecord() {
        Part part1 = new Part("Audi", "A1", null, null, "Name1", "Desc1", 1.0, false, 1, null, null);

        Optional<PartAvailability> expectedResult = Optional.of(new PartAvailability(false, "Not available"));

        when(partRepository.findById(1L)).thenReturn(Optional.of(part1));

        Optional<PartAvailability> result = carPartsService.checkAvailabilityAndShippingDate(1L);
        assertThat(result, samePropertyValuesAs(expectedResult));
    }

    @Test
    void persistServiceActionShouldReturnPersistedActionAfterSave() {
        Part part1 = new Part("Audi", "A1", null, null, "Name1", "Desc1", 1.0, false, 1, null, null);
        Optional<ServiceAction> expectedResult = Optional.of(new ServiceAction("action", new Date(), new Date(), part1));
        ServiceAction serviceActionNullPart = new ServiceAction("action", new Date(), new Date(), null);

        when(partRepository.findById(1L)).thenReturn(Optional.of(part1));

        Optional<ServiceAction> result = carPartsService.persistNewServiceAction(1L, serviceActionNullPart);

        verify(serviceActionRepository, times(1)).save(ArgumentMatchers.any());
        assertThat(result, samePropertyValuesAs(expectedResult));
    }

    @Test
    void shouldNotPersistServiceActionAndReturnOptionalEmptyWhenPartIsNotFound() {
        ServiceAction serviceActionNullPart = new ServiceAction("action", new Date(), new Date(), null);

        when(partRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ServiceAction> result = carPartsService.persistNewServiceAction(1L, serviceActionNullPart);

        verify(serviceActionRepository, times(0)).save(ArgumentMatchers.any());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfActionWithDateRange() {
        ServiceAction serviceAction1 = new ServiceAction("action1", new Date(), new Date(), new Part());
        ServiceAction serviceAction2 = new ServiceAction("action2", new Date(), new Date(), new Part());
        List<ServiceAction> expectedResult = Arrays.asList(serviceAction1, serviceAction2);
        when(serviceActionRepository.findAllActionsByDateRange(any(), any())).thenReturn(Arrays.asList(serviceAction1,serviceAction2));

        List<ServiceAction> actualResult = carPartsService.findAllActionsByDateRange(new Date(), new Date());
        assertThat(actualResult, containsInAnyOrder(expectedResult.toArray()));
    }

    @Test
    void shouldReturnListOfRemovedArguments() {
        Part part1 = new Part("Audi", "A1", null, null, "Name1", "Desc1", 1.0, false, 1, null, null);
        SalesArgument salesArgument1 = new SalesArgument("argument1", part1);
        SalesArgument salesArgument2 = new SalesArgument("argument2", part1);
        List<SalesArgument> expectedResult = Arrays.asList(salesArgument1, salesArgument2);
        when(salesArgumentRepository.deleteByPartId(1L)).thenReturn(expectedResult);

        List<SalesArgument> actualResult = carPartsService.deleteAllArgumentsByPartId(1L);
        assertThat(actualResult, containsInAnyOrder(expectedResult.toArray()));
    }
}