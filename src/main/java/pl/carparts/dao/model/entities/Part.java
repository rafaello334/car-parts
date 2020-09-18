package pl.carparts.dao.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Part(String brand, String model, Integer productionTimeInHoursMin, Integer productionTimeInHoursMax, String name, String description, Double cost, boolean isAvailable, Integer shippingTime, Set<SalesArgument> salesArguments, Set<ServiceAction> serviceActions) {
        this.brand = brand;
        this.model = model;
        this.productionTimeInHoursMin = productionTimeInHoursMin;
        this.productionTimeInHoursMax = productionTimeInHoursMax;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.isAvailable = isAvailable;
        this.shippingTime = shippingTime;
        this.salesArguments = salesArguments;
        this.serviceActions = serviceActions;
    }
}
