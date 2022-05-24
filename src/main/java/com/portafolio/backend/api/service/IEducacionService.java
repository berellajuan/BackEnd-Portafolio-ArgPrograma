package com.portafolio.backend.api.service;

import java.util.List;

import com.portafolio.backend.api.models.Educacion;

public interface IEducacionService {
	public List<Educacion> findAll();
	
	public Educacion findById(Long id);
	
	public Educacion save(Educacion unaEducacion);
	
	public void delete(Long id);
	
}
