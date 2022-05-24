package com.portafolio.backend.api.service;

import java.util.List;

import com.portafolio.backend.api.models.Proyect;

public interface IProyectService {
	public List<Proyect> findAll();

	public Proyect findById(Long id);

	public Proyect save(Proyect unaProyect);

	public void delete(Long id);

}
