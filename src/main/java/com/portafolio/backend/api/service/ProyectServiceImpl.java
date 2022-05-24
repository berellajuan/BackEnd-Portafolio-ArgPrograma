package com.portafolio.backend.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portafolio.backend.api.dao.IProyectDao;
import com.portafolio.backend.api.models.Proyect;

@Service
public class ProyectServiceImpl implements IProyectService{
	
	@Autowired
	private IProyectDao proyectDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Proyect> findAll() {
		
		return (List<Proyect>) proyectDao.findAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Proyect findById(Long id) {
		
		return proyectDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Proyect save(Proyect unaProyect) {
		
		return proyectDao.save(unaProyect);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		proyectDao.deleteById(id);		
	}

}
