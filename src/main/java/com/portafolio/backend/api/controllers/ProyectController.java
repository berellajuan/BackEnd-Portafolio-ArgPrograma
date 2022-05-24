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

import com.portafolio.backend.api.models.Proyect;
import com.portafolio.backend.api.models.vm.Asset;
import com.portafolio.backend.api.service.IProyectService;
import com.portafolio.backend.api.service.S3Service;

@CrossOrigin(origins = { "*"})
@RestController
@RequestMapping("/api/portafolio")
public class ProyectController {
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private IProyectService proyectService;

	@GetMapping("/proyects")
	public List<Proyect> listProyects() {
		return proyectService.findAll();
	}

	@GetMapping("/proyects/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Proyect proyecto = null;
		try {
			proyecto = proyectService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (proyecto == null) {
			response.put("mensaje", "El proyecto ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Proyect>(proyecto, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/proyects")
	public ResponseEntity<?> create(@Valid @RequestBody Proyect proyect,BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		Proyect proyecto = null;
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors",errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			proyecto = proyectService.save(proyect);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El proyecto fue creado con exito!");
		response.put("proyect", proyecto);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/proyects/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Proyect proyect,BindingResult result, @PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		Proyect proyectAct = proyectService.findById(id);
		Proyect proyectActualizado = null;
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors",errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (proyectAct == null) {
			response.put("mensaje", "El proyecto ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {

			proyectAct.setNombre(proyect.getNombre());
			proyectAct.setDescripcion(proyect.getDescripcion());
			proyectAct.setLink(proyect.getLink());
			proyectAct.setFecha(proyect.getFecha());
			proyectAct.setDescripcionCompleta(proyect.getDescripcionCompleta());
			proyectAct.setKeyImagen(proyect.getKeyImagen());
			proyectAct.setImagen(proyect.getImagen());
			
			proyectActualizado = proyectService.save(proyectAct);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El proyecto fue actualizado con exito!");
		response.put("proyect", proyectActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/proyects/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			Proyect pro = proyectService.findById(id);
			String keyAEliminar = pro.getKeyImagen();
			
			if(keyAEliminar != null) {
				s3Service.deleteObject(keyAEliminar);
			}
			
			proyectService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El proyecto fue eliminado con exito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/proyects/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo,@RequestParam("id") Long id){
		Map<String, Object> response = new HashMap<>();
		
		Proyect pro = proyectService.findById(id);
		
		if(!archivo.isEmpty()) {
			String key = null;
			String img = null;		
			try {
				key = s3Service.putObject(archivo);
				img = s3Service.getObjectUrl(key);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
			String keyAnterior = pro.getKeyImagen();
			
			if(keyAnterior != null) {
				s3Service.deleteObject(keyAnterior);
			}
			
			pro.setKeyImagen(key);
			pro.setImagen(img);
			proyectService.save(pro);
			
			response.put("proyect",pro);
			response.put("mensaje","Se subio correctamente la imagen");
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/proyects/img/{nombreImagen:.+}")
	public ResponseEntity<Resource> verLogoEmpresa(@PathVariable String nombreImagen){
		Asset asset = null;
		try {
			asset = s3Service.getObject(nombreImagen);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayResource resource = new ByteArrayResource(asset.getContent());
		return ResponseEntity.ok().header("Content-Type", asset.getContentType())
				.contentLength(asset.getContent().length).body(resource);	
			
	}

}
