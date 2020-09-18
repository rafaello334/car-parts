package pl.carparts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import pl.carparts.dao.model.PartAvailability;
import pl.carparts.dao.model.entities.Part;
import pl.carparts.dao.model.entities.SalesArgument;
import pl.carparts.dao.model.entities.ServiceAction;
import pl.carparts.service.CarPartsService;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarPartsController.class)
public class CarPartsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarPartsService carPartsService;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    @SneakyThrows
    void findByBrandAndModelEndpointSuccessTest() {
        String brand = "Audi";
        String model = "A4";

        Part part1 = getExamplePartUsingBrandModel(brand, model);
        Part part2 = getExamplePartUsingBrandModel(brand, model);
        List<Part> expectedResult = Arrays.asList(part1, part2);

        when(carPartsService.findByBrandAndModel(brand, model)).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/findByBrandAndModel?brand=%s&model=%s", brand, model))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @SneakyThrows
    void findByBrandAndModelEndpointFailEmptyBrandTest() {
        String brand = "";
        String model = "A4";

        mockMvc.perform(get(String.format("/findByBrandAndModel?brand=%s&model=%s", brand, model))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void findByBrandAndModelEndpointFailEmptyModelTest() {
        String brand = "Audi";
        String model = "";

        mockMvc.perform(get(String.format("/findByBrandAndModel?brand=%s&model=%s", brand, model))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void findByBrandAndModelAndNameOrDescriptionSuccessTest() {
        String brand = "Audi";
        String model = "A4";
        String name = "name";
        String description = "description";

        Part part1 = getExamplePartUsingBrandModelNameDescription(brand, model, name, description);
        Part part2 = getExamplePartUsingBrandModelNameDescription(brand, model, name, description);
        List<Part> expectedResult = Arrays.asList(part1, part2);

        when(carPartsService.findByBrandAndModelFilteringByNameAndDescription(brand, model, name, description)).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/findByBrandAndModelAndNameOrDescription?brand=%s&model=%s&name=%s&description=%s", brand, model, name, description))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @SneakyThrows
    void findByBrandAndModelAndNameOrDescriptionFailBrandEmptyTest() {
        String brand = "";
        String model = "A4";
        String name = "name";
        String description = "description";

        mockMvc.perform(get(String.format("/findByBrandAndModelAndNameOrDescription?brand=%s&model=%s&name=%s&description=%s", brand, model, name, description))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void findByBrandAndModelAndNameOrDescriptionFailModelEmptyTest() {
        String brand = "Audi";
        String model = "";
        String name = "name";
        String description = "description";

        mockMvc.perform(get(String.format("/findByBrandAndModelAndNameOrDescription?brand=%s&model=%s&name=%s&description=%s", brand, model, name, description))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void findByBrandSuccessTest() {
        String brand = "Audi";

        Part part1 = getExamplePartUsingBrandModel(brand, "A4");
        Part part2 = getExamplePartUsingBrandModel(brand, "Q7");
        Part part3 = getExamplePartUsingBrandModel(brand, "Q7");

        Map<String, List<Part>> expectedResult = new TreeMap<>();
        expectedResult.put("A4", Collections.singletonList(part1));
        expectedResult.put("Q7", Arrays.asList(part2, part3));

        when(carPartsService.findByBrand(brand)).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/findByBrand?brand=%s", brand))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @SneakyThrows
    void findByBrandFailEmptyBrandTest() {
        String brand = "";

        mockMvc.perform(get(String.format("/findByBrand?brand=%s", brand))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void checkAvailabilityAndShippingDateSuccessTest() {
        Long partId = 1L;
        Optional<PartAvailability> expectedResult = Optional.of(new PartAvailability(true, "2020-09-18"));

        when(carPartsService.checkAvailabilityAndShippingDate(partId)).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/checkAvailabilityAndShippingDate?id=%s", partId))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @SneakyThrows
    void checkAvailabilityAndShippingDateFailInvalidIdTest() {
        Long partId = -1L;
        Optional<PartAvailability> expectedResult = Optional.of(new PartAvailability(true, "2020-09-18"));

        when(carPartsService.checkAvailabilityAndShippingDate(partId)).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/checkAvailabilityAndShippingDate?id=%s", partId))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void checkAvailabilityAndShippingDateFailNotFoundIdTest() {
        Long requestId = 2L;
        Long databaseId = 5L;
        Optional<PartAvailability> expectedResult = Optional.of(new PartAvailability(true, "2020-09-18"));

        when(carPartsService.checkAvailabilityAndShippingDate(databaseId)).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/checkAvailabilityAndShippingDate?id=%s", requestId))
                .accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void findServiceActionsByDateRangeSuccessTest() {
        Date dateFrom = new GregorianCalendar(2020, Calendar.SEPTEMBER, 16).getTime();
        Date dateTo = new GregorianCalendar(2020, Calendar.SEPTEMBER, 18).getTime();

        ServiceAction serviceAction1 = new ServiceAction("action1", new Date(), new Date(), new Part());
        ServiceAction serviceAction2 = new ServiceAction("action2", new Date(), new Date(), new Part());
        List<ServiceAction> expectedResult = Arrays.asList(serviceAction1, serviceAction2);

        when(carPartsService.findAllActionsByDateRange(any(), any())).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/findServiceActionsByDateRange?dateFrom=%s&dateTo=%s", DATE_FORMAT.format(dateFrom), DATE_FORMAT.format(dateTo)))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @SneakyThrows
    void findServiceActionsByDateRangeFailAnyDateNullTest() {
        Date dateTo = new GregorianCalendar(2020, Calendar.SEPTEMBER, 18).getTime();

        ServiceAction serviceAction1 = new ServiceAction("action1", new Date(), new Date(), new Part());
        ServiceAction serviceAction2 = new ServiceAction("action2", new Date(), new Date(), new Part());
        List<ServiceAction> expectedResult = Arrays.asList(serviceAction1, serviceAction2);

        when(carPartsService.findAllActionsByDateRange(any(), any())).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/findServiceActionsByDateRange?dateFrom=%s&dateTo=%s", null, DATE_FORMAT.format(dateTo)))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void findServiceActionsByDateRangeFailDateToBeforeDateFromTest() {
        Date dateFrom = new GregorianCalendar(2020, Calendar.SEPTEMBER, 17).getTime();
        Date dateTo = new GregorianCalendar(2020, Calendar.SEPTEMBER, 14).getTime();

        ServiceAction serviceAction1 = new ServiceAction("action1", new Date(), new Date(), new Part());
        ServiceAction serviceAction2 = new ServiceAction("action2", new Date(), new Date(), new Part());
        List<ServiceAction> expectedResult = Arrays.asList(serviceAction1, serviceAction2);

        when(carPartsService.findAllActionsByDateRange(any(), any())).thenReturn(expectedResult);

        mockMvc.perform(get(String.format("/findServiceActionsByDateRange?dateFrom=%s&dateTo=%s", DATE_FORMAT.format(dateFrom), DATE_FORMAT.format(dateTo)))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void updatePartDescriptionByIdSuccessTest() {
        Long partId = 1L;
        String description = "Desc";

        doNothing().when(carPartsService).updatePartDescription(partId, description);

        mockMvc.perform(patch(String.format("/updatePartDescriptionById?id=%s&description=%s", partId, description))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Record updated successfully"));
    }

    @Test
    @SneakyThrows
    void updatePartDescriptionByIdErrorInvalidIdTest() {
        Long partId = -1L;
        String description = "Desc";

        mockMvc.perform(patch(String.format("/updatePartDescriptionById?id=%s&description=%s", partId, description))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void updatePartDescriptionByIdErrorInvalidDescriptionTest() {
        Long partId = 1L;
        String description = "";

        mockMvc.perform(patch(String.format("/updatePartDescriptionById?id=%s&description=%s", partId, description))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void addServiceActionSuccessTest() {
        Long partId = 1L;
        ServiceAction expectedResult = readResource("input/service-action.json", ServiceAction.class);

        when(carPartsService.persistNewServiceAction(anyLong(), any(ServiceAction.class))).thenReturn(Optional.of(expectedResult));

        mockMvc.perform(post(String.format("/addServiceAction?id=%s", partId))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(expectedResult))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @SneakyThrows
    void addServiceActionFailIdInvalidTest() {
        Long partId = -1L;
        ServiceAction expectedResult = readResource("input/service-action.json", ServiceAction.class);

        mockMvc.perform(post(String.format("/addServiceAction?id=%s", partId))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(expectedResult))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void addServiceActionFailServiceActionInvalidTest() {
        Long partId = 1L;
        String serviceAction = "";

        mockMvc.perform(post(String.format("/addServiceAction?id=%s", partId))
                .contentType("application/json")
                .content(serviceAction)
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void addServiceActionFailPartIdNotFoundTest() {
        Long partId = 1L;
        ServiceAction expectedResult = readResource("input/service-action.json", ServiceAction.class);

        when(carPartsService.persistNewServiceAction(anyLong(), any(ServiceAction.class))).thenReturn(Optional.empty());

        mockMvc.perform(post(String.format("/addServiceAction?id=%s", partId))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(expectedResult))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void deleteArgumentsByPartIdSuccessTest() {
        Long partId = 1L;
        SalesArgument argument1 = new SalesArgument("argument1", getExamplePartUsingBrandModel("Audi", "A4"));
        SalesArgument argument2 = new SalesArgument("argument2", getExamplePartUsingBrandModel("Audi", "A4"));

        List<SalesArgument> expectedResult = Arrays.asList(argument1, argument2);
        when(carPartsService.deleteAllArgumentsByPartId(partId)).thenReturn(expectedResult);

        mockMvc.perform(delete(String.format("/deleteArgumentsByPartId?id=%s", partId))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @SneakyThrows
    void deleteArgumentsByPartIdFailArgumentsNotFoundForPartIdTest() {
        Long partId = 1L;
        SalesArgument argument1 = new SalesArgument("argument1", getExamplePartUsingBrandModel("Audi", "A4"));
        SalesArgument argument2 = new SalesArgument("argument2", getExamplePartUsingBrandModel("Audi", "A4"));

        when(carPartsService.deleteAllArgumentsByPartId(partId)).thenReturn(Collections.emptyList());

        mockMvc.perform(delete(String.format("/deleteArgumentsByPartId?id=%s", partId))
                .accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void deleteArgumentsByPartIdFailInvalidIdTest() {
        Long partId = -1L;

        mockMvc.perform(delete(String.format("/deleteArgumentsByPartId?id=%s", partId))
                .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    private Part getExamplePartUsingBrandModel(String brand, String model) {
        return new Part(brand, model, null, null, "Name1", "Desc1", 1.0, true, 1, null, null);
    }

    private Part getExamplePartUsingBrandModelNameDescription(String brand, String model, String name, String description) {
        return new Part(brand, model, null, null, name, description, 1.0, true, 1, null, null);
    }

    private <T> T readResource(String path, Class<T> valueType) throws IOException {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            return convert(IOUtils.toString(inputStream, StandardCharsets.UTF_8), valueType);
        }
    }

    private <T> T convert(String input, Class<T> valueType) {
        try {
            return this.objectMapper.readValue(input, valueType);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
