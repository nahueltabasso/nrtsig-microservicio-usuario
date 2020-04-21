package nrt.microservicios.usuarios.app.controllers;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import nrt.microservicios.commons.controllers.CommonController;
import nrt.microservicios.main.commons.usuario.entity.Alumno;
import nrt.microservicios.usuarios.app.services.AlumnoService;

@RestController
@RequestMapping("/alumno")
public class AlumnoController extends CommonController<Alumno, AlumnoService> {

	@Autowired
	private AlumnoService alumnoService;
	
	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> addWithPhoto(@Valid Alumno alumno, BindingResult result, @RequestParam MultipartFile archivo) throws IOException {
		if (!archivo.isEmpty()) {
			alumno.setFoto(archivo.getBytes());
		}
		return super.add(alumno, result);
	}
	
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id) {
		Optional<Alumno> alumno = alumnoService.findById(id);
		if (alumno.isEmpty() || alumno.get().getFoto() == null) {
			return ResponseEntity.notFound().build();
		}
		Resource imagen = new ByteArrayResource(alumno.get().getFoto());
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editAlumno(@Valid @RequestBody Alumno alumno, BindingResult result, @PathVariable Long id) throws Exception {
		// Validamos las restricciones de las propiedades del entity Alumno
		if (result.hasErrors()) {
			return this.validar(result);
		}
		Alumno alumnoAct = alumnoService.actualizarAlumno(alumno, id);
		return new ResponseEntity<Alumno>(alumnoAct, HttpStatus.CREATED);
	}
	
	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editWithFoto(@Valid Alumno alumno, BindingResult result, @PathVariable Long id,
			@RequestParam MultipartFile archivo) throws Exception {
		// Validamos las restricciones de las propiedades del entity Alumno
		if (result.hasErrors()) {
			return this.validar(result);
		}
		
		if (!archivo.isEmpty()) {
			alumno.setFoto(archivo.getBytes());
		}
		
		Alumno alumnoAct = alumnoService.actualizarAlumno(alumno, id);
		return new ResponseEntity<Alumno>(alumnoAct, HttpStatus.CREATED);
	}
	
	@GetMapping("/ultimo-legajo")
	public ResponseEntity<?> getUltimoLegajo() {
		Long ultimoLegajo = alumnoService.obtenerUltimoLegajo();
		return new ResponseEntity<Long>(ultimoLegajo, HttpStatus.OK);
	}

}
