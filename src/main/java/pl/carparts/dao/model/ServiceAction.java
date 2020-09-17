package pl.carparts.dao.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class ServiceAction {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Temporal(TemporalType.DATE)
    private Date actionStartDate;

    @Temporal(TemporalType.DATE)
    private Date actionFinishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Part part;
}
