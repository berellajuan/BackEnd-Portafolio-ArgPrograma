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

import com.portafolio.backend.api.models.Experiencia;
import com.portafolio.backend.api.models.vm.Asset;
import com.portafolio.backend.api.service.IExperienciaService;
import com.portafolio.backend.api.service.S3Service;

@CrossOrigin(origins = { "*"})
@RestController
@RequestMapping("/api/portafolio")
public class ExperienciaController {
	
	@Autowired
	private IExperienciaService experienciaService;
	
	@Autowired
	private S3Service s3Service;

	@GetMapping("/experiencias")
	public List<Experiencia> listExperiencias() {
		return experienciaService.findAll();
	}

	@GetMapping("/experiencias/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		Experiencia experiencia = null;

		try {
			experiencia = experienciaService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (experiencia == null) {
			response.put("mensaje","La experiencia ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Experiencia>(experiencia, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/experiencias")
	public ResponseEntity<?> create(@Valid @RequestBody Experiencia exp, BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		Experiencia experiencia = null;
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors",errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			experiencia = experienciaService.save(exp);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en  la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La experiencia fue creada con exito!");
		response.put("experiencia", experiencia);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/experiencias/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Experiencia experiencia,BindingResult result ,@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		Experiencia expActual = experienciaService.findById(id);
		Experiencia expActualizada = null;
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors",errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (expActual == null) {
			response.put("mensaje", "Error, no se puede editar, La experiencia ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			expActual.setTitulo(experiencia.getTitulo());
			expActual.setFecha(experiencia.getFecha());
			expActual.setFechaFin(experiencia.getFechaFin());
			expActual.setDescripcion(experiencia.getDescripcion());
			expActual.setLogoKey(experiencia.getLogoKey());
			expActual.setLogoEmpresa(experiencia.getLogoEmpresa());

			expActualizada = experienciaService.save(expActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la experiencia en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La experiencia fue actualizada con exito!");
		response.put("experiencia", expActualizada);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/experiencias/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			Experiencia exp = experienciaService.findById(id);
			String key = exp.getLogoKey();
			if(key != null) {
				s3Service.deleteObject(key);
			}
			experienciaService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la experiencia en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La experiencia fue eliminada con exito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/experiencias/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo,@RequestParam("id") Long id){
		Map<String, Object> response = new HashMap<>();
		
		Experiencia exp = experienciaService.findById(id);
		
		if(!archivo.isEmpty()) {
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
			
			String keyAnterior = exp.getLogoKey();
			if(keyAnterior != null) {
				s3Service.deleteObject(keyAnterior);
			}
			
			exp.setLogoKey(key);
			exp.setLogoEmpresa(logoImg);
			
			experienciaService.save(exp);
			response.put("experiencia",exp);
			response.put("mensaje","Se subio correctamente la imagen");
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/experiencias/img/{nombreLogo:.+}")
	public ResponseEntity<Resource> verLogoEmpresa(@PathVariable String nombreLogo){
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
