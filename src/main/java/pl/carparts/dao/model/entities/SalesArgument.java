package pl.carparts.dao.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(value = {"id", "part"})
@Entity
public class SalesArgument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String argument;

    @ManyToOne(fetch = FetchType.LAZY)
    private Part part;

    public SalesArgument(String argument, Part part) {
        this.argument = argument;
        this.part = part;
    }
}
