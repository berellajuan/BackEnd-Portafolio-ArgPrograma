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
@Table(name="proyects")
public class Proyect implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="El nombre del proyecto no debe estar vacio")
	@Size(min=5,max=60,message="El nombre del proyecto no debe tener al menos 5 caracteres")
	@Column(nullable=false)
	private String nombre;
	@NotEmpty(message="La descripcion no debe estar vacia")
	@Size(min=10,max=100,message="La descripcion debe tener un minimo de 10 caracteres hasta 100")
	@Column(nullable=false)
	private String descripcion;
	@NotEmpty(message="El enlace al proyecto no debe estar vacio")
	@Size(min=10,max=80,message="El enlace al proyecto debe tener al menos 10 caracteres")
	@Column(nullable=false)
	private String link;
	
	@NotEmpty(message="La descripcion completa no debe estar vacia")
	@Size(min=10,max=255,message="La descripcion completa debe tener un minimo de 10 caracteres hasta 255")
	@Column(nullable=false)
	private String descripcionCompleta;
	
	private String keyImagen;
	private String imagen;	
	
	@NotNull(message="La fecha no puede estar vacia")
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	public String getKeyImagen() {
		return keyImagen;
	}

	public void setKeyImagen(String keyImagen) {
		this.keyImagen = keyImagen;
	}

	public String getDescripcionCompleta() {
		return descripcionCompleta;
	}

	public void setDescripcionCompleta(String descripcionCompleta) {
		this.descripcionCompleta = descripcionCompleta;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
