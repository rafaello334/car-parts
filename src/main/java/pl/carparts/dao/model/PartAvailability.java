package pl.carparts.dao.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartAvailability {
    private boolean availability;
    private String shippingDate;
}
