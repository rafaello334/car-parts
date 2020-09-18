package pl.carparts.dao.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(value = {"id", "part"})
@ToString
@Entity
public class SalesArgument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String argument;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Part part;

    public SalesArgument(String argument, Part part) {
        this.argument = argument;
        this.part = part;
    }
}
