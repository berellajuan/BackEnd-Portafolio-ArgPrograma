package com.portafolio.backend.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portafolio.backend.api.dao.IEducacionDao;
import com.portafolio.backend.api.models.Educacion;

@Service
public class EducacionServiceImpl implements IEducacionService{
	
	@Autowired
	private IEducacionDao educacionDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Educacion> findAll() {
		return (List<Educacion>) educacionDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Educacion findById(Long id) {
		
		return educacionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Educacion save(Educacion unaEducacion) {
		
		return educacionDao.save(unaEducacion);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		educacionDao.deleteById(id);		
	}

}
