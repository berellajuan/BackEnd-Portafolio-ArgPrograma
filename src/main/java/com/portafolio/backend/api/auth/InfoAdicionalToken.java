package com.portafolio.backend.api.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.portafolio.backend.api.models.Usuario;
import com.portafolio.backend.api.service.IUsuarioService;

@Component
public class InfoAdicionalToken implements TokenEnhancer{
	
	@Autowired
	private IUsuarioService userService;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		Usuario usuario = userService.findByUsername(authentication.getName());
		
		Map<String,Object> info = new HashMap<>();
		info.put("info", "Este OAuth2 fue implementado por Juan Cruz Berella para ARG PROGRAMA #YO PROGRAMO");
		
		info.put("nombre_usuario", "Hola " + usuario.getUsername() + " espero que disfrute su edicion!");
		
		info.put("nombre", usuario.getNombre());
		info.put("apellido", usuario.getApellido());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}

}
