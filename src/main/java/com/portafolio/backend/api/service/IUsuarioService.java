package com.portafolio.backend.api.service;

import com.portafolio.backend.api.models.Usuario;

public interface IUsuarioService {
	public Usuario findByUsername(String username);
}
