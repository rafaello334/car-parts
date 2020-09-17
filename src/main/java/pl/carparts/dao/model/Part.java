package pl.carparts.dao.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Part {
    @Id
    @GeneratedValue
    private Long id;
    private String brand;
    private String model;
    private Integer productionTimeInHoursMin;
    private Integer productionTimeInHoursMax;
    private String name;
    private String description;
    private Double cost;
    private boolean isAvailable;
    private Integer shippingTime;

    @OneToMany(mappedBy = "part", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SalesArgument> salesArguments;

    @OneToMany(mappedBy = "part", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ServiceAction> serviceActions;
}
