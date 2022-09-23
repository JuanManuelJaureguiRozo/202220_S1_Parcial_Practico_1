package co.edu.uniandes.dse.parcialejemplo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialejemplo.entities.HabitacionEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.HotelEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.repositories.HabitacionRepository;
import co.edu.uniandes.dse.parcialejemplo.repositories.HotelRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HotelHabitacionService {

	@Autowired
	HabitacionRepository habitacionRepository;

	@Autowired
	HotelRepository hotelRepository;

    @Transactional
	public HabitacionEntity addHabitacion(Long hotelId, Long habitacionId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle una habitacion al hotel con id = {0}", hotelId);
		Optional<HotelEntity> hotelEntity = hotelRepository.findById(hotelId);
		Optional<HabitacionEntity> habitacionEntity = habitacionRepository.findById(habitacionId);

		if (hotelEntity.isEmpty())
			throw new EntityNotFoundException("El hotel con el id dado no se encontró");

		if (habitacionEntity.isEmpty())
			throw new EntityNotFoundException("La habitación con el id dado no se encontró");

		hotelEntity.get().getHabitaciones().add(habitacionEntity.get());
		log.info("Termina proceso de asociarle una habitación al hotel con id = {0}", hotelId);
		
        return habitacionEntity.get(); 
	}
}