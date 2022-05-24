package com.portafolio.backend.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portafolio.backend.api.dao.IExperienciaDao;
import com.portafolio.backend.api.models.Experiencia;

@Service
public class ExperienciaServiceImpl implements IExperienciaService{
	
	@Autowired
	private IExperienciaDao experienciaDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Experiencia> findAll() {
		return (List<Experiencia>) experienciaDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Experiencia findById(Long id) {
		
		return experienciaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Experiencia save(Experiencia unaExperiencia) {
		
		return experienciaDao.save(unaExperiencia);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		experienciaDao.deleteById(id);		
	}

}
