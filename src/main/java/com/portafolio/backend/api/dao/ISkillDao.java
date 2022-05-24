package com.portafolio.backend.api.dao;

import org.springframework.data.repository.CrudRepository;

import com.portafolio.backend.api.models.Skill;

public interface ISkillDao extends CrudRepository<Skill,Long>{

}
