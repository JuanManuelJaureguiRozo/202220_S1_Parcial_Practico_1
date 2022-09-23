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

import co.edu.uniandes.dse.parcialejemplo.entities.HotelEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(HotelService.class)
public class HotelServiceTest {

    @Autowired
	private HotelService hotelService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<HotelEntity> hotelList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from HotelEntity");
	}

    private void insertData() {
		for (int i = 0; i < 3; i++) {
			HotelEntity hotelEntity = factory.manufacturePojo(HotelEntity.class);
			entityManager.persist(hotelEntity);
			hotelList.add(hotelEntity);
		}
	}

    @Test
	void testCreateHotel() throws EntityNotFoundException, IllegalOperationException {
		HotelEntity newEntity = factory.manufacturePojo(HotelEntity.class);
		newEntity.setNombre("Holiday Inn");
		HotelEntity resultado = hotelService.createHotel(newEntity);
		assertNotNull(resultado);
		HotelEntity entity = entityManager.find(HotelEntity.class, resultado.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
		assertEquals(newEntity.getDireccion(), entity.getDireccion());
		assertEquals(newEntity.getEstrellas(), entity.getEstrellas());
	}

    @Test
	void testCreateHotelWithSameName() {
		assertThrows(IllegalOperationException.class, () -> {
			HotelEntity newEntity = factory.manufacturePojo(HotelEntity.class);
			newEntity.setNombre("Holiday Inn");
			hotelService.createHotel(newEntity);
		});
	}

    
}
