package com.portafolio.backend.api.service;

import java.util.List;

import com.portafolio.backend.api.models.Experiencia;

public interface IExperienciaService {
	
	public List<Experiencia> findAll();
	
	public Experiencia findById(Long id);
	
	public Experiencia save(Experiencia unaExperiencia);
	
	public void delete(Long id);
	
}
