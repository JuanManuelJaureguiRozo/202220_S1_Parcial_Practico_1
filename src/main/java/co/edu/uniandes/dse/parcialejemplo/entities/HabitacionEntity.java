package co.edu.uniandes.dse.parcialejemplo.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

@Getter
@Setter
@Entity
public class HabitacionEntity extends BaseEntity {

    private String identificacion;
    private Integer personas;
    private Integer camas;
    private Integer banos;

	@PodamExclude
	@ManyToOne
	private HotelEntity hotel;
    
}
