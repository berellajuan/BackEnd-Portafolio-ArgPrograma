package com.portafolio.backend.api.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="experiencias")
public class Experiencia implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="El nombre de la entidad no debe estar vacio")
	@Size(min=3,max=60,message="El nombre del a entidad debe tener mas de 3 caracteres")
	@Column(nullable=false)
	private String titulo;
	
	@NotNull(message="La fecha de inicio no puede estar vacia")
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@NotNull(message="La fecha de finalizacion no puede estar vacia")
	@Temporal(TemporalType.DATE)
	private Date fechaFin;
	
	private String LogoKey;
	private String logoEmpresa;
	
	@NotEmpty(message="La informacion adicional no debe estar vacia")
	@Size(min=10,max=255,message="La informacion adicional debe tener mas de 10 caracteres")
	@Column(nullable=false)
	private String descripcion;
	
	public String getLogoKey() {
		return LogoKey;
	}

	public void setLogoKey(String logoKey) {
		LogoKey = logoKey;
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public String getLogoEmpresa() {
		return logoEmpresa;
	}
	
	public void setLogoEmpresa(String logoEmpresa) {
		this.logoEmpresa = logoEmpresa;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
