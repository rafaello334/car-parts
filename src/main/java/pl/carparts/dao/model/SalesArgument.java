package pl.carparts.dao.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class SalesArgument {
    @Id
    @GeneratedValue
    private Long id;
    private String argument;

    @ManyToOne(fetch = FetchType.LAZY)
    private Part part;
}
