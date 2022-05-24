package com.portafolio.backend.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portafolio.backend.api.dao.ISkillDao;
import com.portafolio.backend.api.models.Skill;

@Service
public class SkillServiceImpl implements ISkillService{
	
	@Autowired
	private ISkillDao skillDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Skill> findAll() {
	
		return (List<Skill>) skillDao.findAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Skill findById(Long id) {
		
		return skillDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Skill save(Skill unaSkill) {
		
		return skillDao.save(unaSkill);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		skillDao.deleteById(id);		
	}


}
