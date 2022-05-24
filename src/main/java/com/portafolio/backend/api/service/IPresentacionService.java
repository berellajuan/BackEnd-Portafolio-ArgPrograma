package com.portafolio.backend.api.service;

import java.util.List;

import com.portafolio.backend.api.models.Presentacion;
import com.portafolio.backend.api.models.Provincia;

public interface IPresentacionService {
	public List<Presentacion> findAll();
	
	public Presentacion save(Presentacion unaPresentacion);
	
	public Presentacion findById(Long id);
	
	public List<Provincia> findAllProvincias();
}
