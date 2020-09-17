package pl.carparts.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.carparts.dao.model.Part;
import pl.carparts.service.CarPartsService;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;


@RestController
@AllArgsConstructor
public class CarPartsController {

    private CarPartsService service;

    @GetMapping("/findByBrandAndModel")
    @ApiOperation(response = List.class, notes = "Permits to get all parts by brand and model", value = "List of parts")
    public ResponseEntity<List<Part>> findByBrandAndModel(@ApiParam(value = "Car brand", required = true) @RequestParam String brand,
                                                          @ApiParam(value = "Car model", required = true) @RequestParam String model) {
        if(isEmpty(brand) || isEmpty(model)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.findByBrandAndModel(brand, model));
    }

    @GetMapping("/findByBrandAndModelAndNameOrDescription")
    @ApiOperation(response = List.class, notes = "Permits to get all parts by brand and model and name or description (Name and description might be partial", value = "List of parts")
    public ResponseEntity<List<Part>> findByBrandAndModelAndNameOrDescription(@ApiParam(value = "Car brand", required = true) @RequestParam String brand,
                                                                              @ApiParam(value = "Car model", required = true) @RequestParam String model,
                                                                              @ApiParam(value = "Part name") @RequestParam String name,
                                                                              @ApiParam(value = "Part description") @RequestParam String description) {
        if(isEmpty(brand) || isEmpty(model)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.findByBrandAndModelAndNameContainsOrDescriptionContains(brand, model, name, description));
    }
}
