package com.portafolio.backend.api.dao;

import org.springframework.data.repository.CrudRepository;

import com.portafolio.backend.api.models.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	public Usuario findByUsername(String username);
}
