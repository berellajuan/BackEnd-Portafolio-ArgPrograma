package com.portafolio.backend.api.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name="skills")
public class Skill implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "El nombre de la skill no debe estar vacio")
	@Size(min=1,max=80,message = "El nombre de la skill debe tener al menos 1 caracter hasta 80")
	@Column(nullable=false)
	private String nombre;
	
	@NotNull(message = "El porcentaje no debe estar vacio")
	@Range(max = 100, min = 0, message = "El porcentaje debe estar entre 0 y 100")
	@Column(nullable=false)
	private Long porcentaje;
	
	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Long porcentaje) {
		this.porcentaje = porcentaje;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
