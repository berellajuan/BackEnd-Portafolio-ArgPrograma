package com.portafolio.backend.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portafolio.backend.api.dao.IPresentacionDao;
import com.portafolio.backend.api.models.Presentacion;
import com.portafolio.backend.api.models.Provincia;

@Service
public class PresentacionServiceImpl implements IPresentacionService {
	
	@Autowired
	private IPresentacionDao presentacionDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Presentacion> findAll() {
		return  (List<Presentacion>) presentacionDao.findAll();
	}

	@Override
	@Transactional
	public Presentacion save(Presentacion unaPresentacion) {
		return presentacionDao.save(unaPresentacion);
	}

	@Override
	@Transactional(readOnly=true)
	public Presentacion findById(Long id) {
		
		return  presentacionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Provincia> findAllProvincias() {
		return presentacionDao.findAllProvincias();
	}
	
	

}
