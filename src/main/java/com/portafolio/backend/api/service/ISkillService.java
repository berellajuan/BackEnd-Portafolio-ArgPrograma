package com.portafolio.backend.api.service;

import java.util.List;

import com.portafolio.backend.api.models.Skill;

public interface ISkillService {
	public List<Skill> findAll();
	
	public Skill findById(Long id);

	public Skill save(Skill unaSkill);

	public void delete(Long id);
	
}
