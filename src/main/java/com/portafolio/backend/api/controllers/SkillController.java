package com.portafolio.backend.api.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.portafolio.backend.api.models.Skill;
import com.portafolio.backend.api.service.ISkillService;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/api/portafolio")
public class SkillController {

	@Autowired
	private ISkillService skillService;

	@GetMapping("/skills")
	public List<Skill> listSkills() {
		return skillService.findAll();
	}
	
	@GetMapping("/skills/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Map<String,Object> response = new HashMap<>();
		Skill skill = null;
		
		try {
			skill = skillService.findById(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(skill == null) {
			response.put("mensaje", "La habilidad ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Skill>(skill, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/skills")
	public ResponseEntity<?> create(@Valid @RequestBody Skill skill,BindingResult result) {
		Map<String,Object> response = new HashMap<>();
		Skill skillPost = null;
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors",errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			skillPost =skillService.save(skill);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La habilidad fue creada con exito!");
		response.put("skill", skillPost);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/skills/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Skill skill,BindingResult result, @PathVariable Long id) {
		Map<String,Object> response = new HashMap<>();
		
		Skill skillAct = skillService.findById(id);
		Skill skillActualizada = null;
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors",errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			skillAct.setNombre(skill.getNombre());
			skillAct.setPorcentaje(skill.getPorcentaje());
			skillActualizada = skillService.save(skillAct);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La habilidad fue actualizada con exito!");
		response.put("skill", skillActualizada);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);		
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/skills/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String,Object> response = new HashMap<>();
		try {
			skillService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
