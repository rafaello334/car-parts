package pl.carparts.dao.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PartAvailability {
    private boolean availability;
    private String shippingDate;
}
