package pl.carparts.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@JsonIgnoreProperties(value = {"id", "part"})
@Entity
public class SalesArgument {
    @Id
    @GeneratedValue
    private Long id;
    private String argument;

    @ManyToOne(fetch = FetchType.LAZY)
    private Part part;
}
