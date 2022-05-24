package com.portafolio.backend.api.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portafolio.backend.api.models.Educacion;
import com.portafolio.backend.api.models.vm.Asset;
import com.portafolio.backend.api.service.IEducacionService;
import com.portafolio.backend.api.service.S3Service;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/portafolio")
public class EducacionController {

	@Autowired
	private IEducacionService educacionService;

	@Autowired
	private S3Service s3Service;

	@GetMapping("/educaciones")
	public List<Educacion> listEducaciones() {
		return educacionService.findAll();
	}

	@GetMapping("/educaciones/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Educacion educacion = null;
		try {
			educacion = educacionService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (educacion == null) {
			response.put("mensaje",
					"La educacion ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Educacion>(educacion, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/educaciones")
	public ResponseEntity<?> create(@Valid @RequestBody Educacion educacion, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		Educacion edu = null;

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			edu = educacionService.save(educacion);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La educacion fue creada con exito!");
		response.put("educacion", edu);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/educaciones/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Educacion educacion, BindingResult result,
			@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		Educacion educacionAct = educacionService.findById(id);
		Educacion educacionActualizada = null;

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (educacionAct == null) {
			response.put("mensaje", "Error, no se puede editar, La educacion ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			educacionAct.setTitulo(educacion.getTitulo());
			educacionAct.setFecha(educacion.getFecha());
			educacionAct.setFechaFin(educacion.getFechaFin());
			educacionAct.setDescripcion(educacion.getDescripcion());
			educacionAct.setLogoKey(educacion.getLogoKey());
			educacionAct.setLogoInstituto(educacion.getLogoInstituto());

			educacionActualizada = educacionService.save(educacionAct);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la educacion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La educacion fue actualizada con exito!");
		response.put("educacion", educacionActualizada);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/educaciones/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			Educacion edu = educacionService.findById(id);
			String keyAEliminar = edu.getLogoKey();
			if(keyAEliminar != null) {
				s3Service.deleteObject(keyAEliminar);				
			}
			educacionService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al borrar la educacion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La educacion fue eliminada con exito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/educaciones/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();

		Educacion edu = educacionService.findById(id);

		if (!archivo.isEmpty()) {
			String key = null;
			String logoImg = null;
			try {
				key = s3Service.putObject(archivo);
				logoImg = s3Service.getObjectUrl(key);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			String keyAnterior = edu.getLogoKey();
			if (keyAnterior != null) {
				s3Service.deleteObject(keyAnterior);
			}

			edu.setLogoKey(key);
			edu.setLogoInstituto(logoImg);

			educacionService.save(edu);
			response.put("educacion", edu);
			response.put("mensaje", "Se subio correctamente la imagen");
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/uploads/educaciones/img/{nombreLogo:.+}")
	public ResponseEntity<Resource> verLogoInstituto(@PathVariable String nombreLogo) {
		Asset asset = null;
		try {
			asset = s3Service.getObject(nombreLogo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayResource resource = new ByteArrayResource(asset.getContent());
		return ResponseEntity.ok().header("Content-Type", asset.getContentType())
				.contentLength(asset.getContent().length).body(resource);
	}

}
