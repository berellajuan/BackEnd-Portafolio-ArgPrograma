package com.portafolio.backend.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.portafolio.backend.api.models.Presentacion;
import com.portafolio.backend.api.models.Provincia;

public interface IPresentacionDao extends CrudRepository<Presentacion,Long>{
	
	@Query("from Provincia")
	public List<Provincia> findAllProvincias();
}
