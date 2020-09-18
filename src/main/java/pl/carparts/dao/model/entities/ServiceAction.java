package pl.carparts.dao.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(value = {"id", "part"})
@Entity
public class ServiceAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date actionStartDate;

    @Temporal(TemporalType.DATE)
    private Date actionFinishDate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(hidden = true)
    private Part part;

    public ServiceAction(String name, Date actionStartDate, Date actionFinishDate, Part part) {
        this.name = name;
        this.actionStartDate = actionStartDate;
        this.actionFinishDate = actionFinishDate;
        this.part = part;
    }
}
