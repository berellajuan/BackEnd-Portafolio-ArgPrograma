package com.portafolio.backend.api.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="presentaciones")
public class Presentacion implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String keyFotoPortada;
	private String fotoPortada;
	private String keyFotoPerfil;
	private String fotoPerfil;
	private String linkCv;
	
	@Column(nullable=false)
	private String nombre;
	
	
	@Column(nullable=false)
	private String titulo;
	
	@Column(nullable=false)
	private String carta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="provincia_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Provincia provincia;	

	public String getLinkCv() {
		return linkCv;
	}

	public void setLinkCv(String linkCv) {
		this.linkCv = linkCv;
	}

	public String getKeyFotoPortada() {
		return keyFotoPortada;
	}

	public void setKeyFotoPortada(String keyFotoPortada) {
		this.keyFotoPortada = keyFotoPortada;
	}

	public String getKeyFotoPerfil() {
		return keyFotoPerfil;
	}

	public void setKeyFotoPerfil(String keyFotoPerfil) {
		this.keyFotoPerfil = keyFotoPerfil;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFotoPortada() {
		return fotoPortada;
	}

	public void setFotoPortada(String fotoPortada) {
		this.fotoPortada = fotoPortada;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCarta() {
		return carta;
	}

	public void setCarta(String carta) {
		this.carta = carta;
	}
	
	
	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
