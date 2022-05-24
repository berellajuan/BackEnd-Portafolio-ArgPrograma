package com.portafolio.backend.api.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.portafolio.backend.api.models.Presentacion;
import com.portafolio.backend.api.models.Provincia;
import com.portafolio.backend.api.models.vm.Asset;
import com.portafolio.backend.api.service.IPresentacionService;
import com.portafolio.backend.api.service.S3Service;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/api/portafolio")
public class PresentacionController {
	
	@Autowired
	private IPresentacionService presentacionService;
	
	@Autowired
	private S3Service s3Service;
	
	@GetMapping("/presentacion")
	public Presentacion showPresentacion(){
		List<Presentacion>  presentacion = presentacionService.findAll();
		return presentacion.get(0);
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/presentacion")
	public ResponseEntity<?> updateHeader( @RequestBody Presentacion presentacion) {
		Map<String, Object> response = new HashMap<>();
		
		Presentacion presentacionAct = presentacionService.findById(1L);
		Presentacion prestActualizada =null;
		
		if(presentacionAct == null) {
			response.put("mensaje", "Error no se puede editar la presentacion en la base de datos no existe!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			presentacionAct.setKeyFotoPortada(presentacion.getKeyFotoPortada());
			presentacionAct.setFotoPortada(presentacion.getFotoPortada());
			presentacionAct.setKeyFotoPerfil(presentacion.getKeyFotoPerfil());
			presentacionAct.setFotoPerfil(presentacion.getFotoPerfil());
			presentacionAct.setLinkCv(presentacion.getLinkCv());
			presentacionAct.setNombre(presentacion.getNombre());
			presentacionAct.setTitulo(presentacion.getTitulo());
			presentacionAct.setProvincia(presentacion.getProvincia());

			
			prestActualizada = presentacionService.save(presentacionAct);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la presentacion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
		response.put("mensaje", "La experiencia fue actualizada con exito!");
		response.put("presentacion", prestActualizada);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/presentacion/carta")
	public ResponseEntity<?> updateCarta(@RequestBody Presentacion presentacion) {
		Map<String, Object> response = new HashMap<>();
		Presentacion presentacionAct = presentacionService.findById(1L);
		Presentacion prestActualizada =null;
		
		if(presentacionAct == null) {
			response.put("mensaje", "Error no se puede editar la carta en la base de datos no existe!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			 presentacionAct.setCarta(presentacion.getCarta());
			 prestActualizada = presentacionService.save(presentacionAct);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la carta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La carta fue actualizada con exito!");
		response.put("presentacion", prestActualizada);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/presentacion/upload/fotoportada")
	public ResponseEntity<?> uploadPortada(@RequestParam("archivo") MultipartFile archivo,@RequestParam("id") Long id){
		Map<String, Object> response = new HashMap<>();
		
		Presentacion pres = presentacionService.findById(id);
		
		if(!archivo.isEmpty()) {
			String key = null;
			String portadaImg = null;
			
			try {
				key = s3Service.putObject(archivo);
				portadaImg = s3Service.getObjectUrl(key);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String keyAnterior = pres.getKeyFotoPortada();
			if(keyAnterior != null) {
				s3Service.deleteObject(keyAnterior);
			}
			
			pres.setKeyFotoPortada(key);
			pres.setFotoPortada(portadaImg);
			
			presentacionService.save(pres);
			
			response.put("presentacion",pres);
			response.put("mensaje","Se subio correctamente la imagen");
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/presentacion/upload/fotoperfil")
	public ResponseEntity<?> uploadPerfil(@RequestParam("archivo") MultipartFile archivo,@RequestParam("id") Long id){
		Map<String, Object> response = new HashMap<>();
		
		Presentacion pres = presentacionService.findById(id);
		
		if(!archivo.isEmpty()) {
			String key = null;
			String perfilImg= null;
			
			try {
				key = s3Service.putObject(archivo);
				perfilImg = s3Service.getObjectUrl(key);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String keyAnterior = pres.getKeyFotoPerfil();
			if(keyAnterior != null) {
				s3Service.deleteObject(keyAnterior);
			}
			
			pres.setKeyFotoPerfil(key);
			pres.setFotoPerfil(perfilImg);
			presentacionService.save(pres);
			
			response.put("presentacion",pres);
			response.put("mensaje","Se subio correctamente la imagen");
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/presentacion/portada/{nombreLogo:.+}")
	public ResponseEntity<Resource> verFotoPortada(@PathVariable String nombreLogo){
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
	
	@GetMapping("/uploads/presentacion/perfil/{nombreLogo:.+}")
	public ResponseEntity<Resource> verFotoPerfil(@PathVariable String nombreLogo){
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
	
	@GetMapping("/presentacion/provincias")
	public List<Provincia> listarProvincias(){
		return presentacionService.findAllProvincias();
	}
	
	
	
}
