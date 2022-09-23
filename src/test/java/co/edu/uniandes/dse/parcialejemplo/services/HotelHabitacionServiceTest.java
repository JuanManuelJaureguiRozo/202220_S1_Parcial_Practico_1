package co.edu.uniandes.dse.parcialejemplo.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import co.edu.uniandes.dse.parcialejemplo.entities.HabitacionEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.HotelEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ HotelHabitacionService.class, HabitacionService.class })
public class HotelHabitacionServiceTest {

    @Autowired
	private HotelHabitacionService hotelHabitacionService;
	
    @Autowired
	private HabitacionService habitacionService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private HotelEntity hotel = new HotelEntity();
	private List<HabitacionEntity> habitacionList = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    /**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from HotelEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from HabitacionEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		hotel = factory.manufacturePojo(HotelEntity.class);
		entityManager.persist(hotel);

		for (int i = 0; i < 3; i++) {
			HabitacionEntity entity = factory.manufacturePojo(HabitacionEntity.class);
			entityManager.persist(entity);
			entity.getHotel();
			habitacionList.add(entity);
			hotel.getHabitaciones().add(entity);
		}
	}
    
    @Test
	void testAddHabitacion() throws EntityNotFoundException, IllegalOperationException {
		HabitacionEntity newHabitacion = factory.manufacturePojo(HabitacionEntity.class);
		habitacionService.createHabitacion(newHabitacion);

		HabitacionEntity habitacionEntity = hotelHabitacionService.addHabitacion(hotel.getId(), newHabitacion.getId());
		assertNotNull(habitacionEntity);

		assertEquals(habitacionEntity.getId(), newHabitacion.getId());
		assertEquals(habitacionEntity.getIdentificacion(), newHabitacion.getIdentificacion());
		assertEquals(habitacionEntity.getPersonas(), newHabitacion.getPersonas());
        assertEquals(habitacionEntity.getCamas(), newHabitacion.getCamas());
        assertEquals(habitacionEntity.getBanos(), newHabitacion.getBanos());
	}

    @Test
	void testAddHabitacionInvalidHotel() {
		assertThrows(EntityNotFoundException.class, () -> {
			HabitacionEntity newHabitacion = factory.manufacturePojo(HabitacionEntity.class);
			habitacionService.createHabitacion(newHabitacion);
			hotelHabitacionService.addHabitacion(0L, newHabitacion.getId());
		});
	}

    @Test
	void testAddInvalidHabitacion() {
		assertThrows(EntityNotFoundException.class, () -> {
			hotelHabitacionService.addHabitacion(hotel.getId(), 0L);
		});
	}
}
