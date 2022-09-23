package co.edu.uniandes.dse.parcialejemplo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialejemplo.entities.HabitacionEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.HabitacionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HabitacionService {

	@Autowired
	HabitacionRepository habitacionRepository;
	
	@Transactional
	public HabitacionEntity createHabitacion(HabitacionEntity habitacionEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación de la habitación");

        if (habitacionEntity.getBanos()>=habitacionEntity.getPersonas())
            throw new IllegalOperationException("El número de baños es mayor o igual al número de personas");

		log.info("Termina proceso de creación de la habitación");
		return habitacionRepository.save(habitacionEntity);

	}
}
