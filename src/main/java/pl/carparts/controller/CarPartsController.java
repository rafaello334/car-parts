package pl.carparts.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.carparts.dao.model.PartAvailability;
import pl.carparts.dao.model.entities.Part;
import pl.carparts.dao.model.entities.SalesArgument;
import pl.carparts.dao.model.entities.ServiceAction;
import pl.carparts.service.CarPartsService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;


@RestController
@AllArgsConstructor
public class CarPartsController {

    private CarPartsService service;

    @GetMapping("/findByBrandAndModel")
    @ApiOperation(response = List.class, notes = "Permits to get all parts by brand and model", value = "List of parts")
    public ResponseEntity<List<Part>> findByBrandAndModel(
            @ApiParam(value = "Car brand", required = true) @RequestParam String brand,
            @ApiParam(value = "Car model", required = true) @RequestParam String model) {
        if(isEmpty(brand) || isEmpty(model)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.findByBrandAndModel(brand, model));
    }

    @GetMapping("/findByBrandAndModelAndNameOrDescription")
    @ApiOperation(response = List.class, notes = "Permits to get all parts by brand and model and name or description (Name and description might be partial",
            value = "List of parts")
    public ResponseEntity<List<Part>> findByBrandAndModelAndNameOrDescription(
            @ApiParam(value = "Car brand", required = true) @RequestParam String brand,
            @ApiParam(value = "Car model", required = true) @RequestParam String model,
            @ApiParam(value = "Part name") @RequestParam(required = false) String name,
            @ApiParam(value = "Part description") @RequestParam(required = false) String description) {

        if(isEmpty(brand) || isEmpty(model)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(service.findByBrandAndModelFilteringByNameAndDescription(brand, model, name, description));
    }

    @GetMapping("/findByBrand")
    @ApiOperation(response = List.class, notes = "Permits to get all models and their parts by brand name",
            value = "List of all models and their parts")
    public ResponseEntity<Map<String, List<Part>>> findByBrand(@ApiParam(value = "Car brand", required = true) @RequestParam String brand) {
        if(isEmpty(brand)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(service.findByBrand(brand));
    }

    @GetMapping("/checkAvailabilityAndShippingDate")
    @ApiOperation(response = List.class, notes = "Permits to get part availability and shipping date by part id",
            value = "Availability and possible shipping date")
    public ResponseEntity<PartAvailability> checkAvailabilityAndShippingDate(
            @ApiParam(value = "Part id", required = true) @RequestParam Long id) {

        if(id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<PartAvailability> partAvailibity = service.checkAvailabilityAndShippingDate(id);
        if(partAvailibity.isPresent()) {
            return ResponseEntity.ok(partAvailibity.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findServiceActionsByDateRange")
    @ApiOperation(response = List.class, notes = "Permits to get all service actions by date range", value = "All service actions")
    public ResponseEntity<List<ServiceAction>> findServiceActionsByDateRange(
            @ApiParam(value = "Date from ISO-8601 format (YYYY-MM-DD)", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date dateFrom,
            @ApiParam(value = "Date to ISO-8601 format (YYYY-MM-DD)", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date dateTo) {

        if(dateFrom == null || dateTo == null || dateFrom.after(dateTo)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.findAllActionsByDateRange(dateFrom, dateTo));
    }

    @PatchMapping("/updatePartDescriptionById")
    @ApiOperation(response = String.class, notes = "Permits to update part description by id", value = "Information about update")
    public ResponseEntity<String> updatePartDescriptionById(
            @ApiParam(value = "Part id", required = true) @RequestParam Long id,
            @ApiParam(value = "Part description", required = true) @RequestParam String description) {

        if(id == null || id <= 0 || isEmpty(description)) {
            return ResponseEntity.badRequest().build();
        }
        service.updatePartDescription(id, description);
        return ResponseEntity.ok("Record updated successfully");
    }

    @PostMapping("/addServiceAction")
    @ApiOperation(notes = "Permits to store new service action for specific part id", value = "Persisted service action")
    public ResponseEntity<ServiceAction> persistServiceAction(
            @ApiParam(value = "Part id", required = true) @RequestParam Long id,
            @RequestBody ServiceAction serviceAction) {

        if(id == null || id <= 0 || serviceAction == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ServiceAction> persistedServiceAction = service.persistNewServiceAction(id, serviceAction);
        if(persistedServiceAction.isPresent()) {
            return ResponseEntity.ok(persistedServiceAction.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteArgumentsByPartId")
    @ApiOperation(response = List.class, notes = "Removes all arguments for specific part. ", value = "Removed arguments")
    public ResponseEntity<List<SalesArgument>> deleteArgumentsByPartId(
            @ApiParam(value = "Part id", required = true) @RequestParam Long id) {

        if(id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<SalesArgument> removedSalesArguments = service.deleteAllArgumentsByPartId(id);
        if(removedSalesArguments.size() == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(removedSalesArguments);
    }
}
