package nrt.microservicios.usuarios.app.models.repository;

import org.springframework.data.repository.CrudRepository;

import nrt.microservicios.usuarios.app.models.entity.Domicilio;

public interface DomicilioRepository extends CrudRepository<Domicilio, Long> {

}