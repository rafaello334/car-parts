package pl.carparts.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(value = {"id", "part"})
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
