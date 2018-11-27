package model;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.uqbarproject.jpa.java8.extras.convert.LocalDateConverter;

@Entity
@DiscriminatorValue("2")
public class Administrador extends Usuario {
	private String nombre;
	private String apellido;
	private String domicilio;
	private String numeroIdentificacion;
	
	@Convert(converter=LocalDateConverter.class)
	private LocalDate fechaAlta;


	public Administrador(String nombre, String apellido, String domicilio, String numeroIdentificacion, LocalDate fechaAlta) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.domicilio = domicilio;
		this.numeroIdentificacion = numeroIdentificacion;
		this.fechaAlta = fechaAlta;
	}
	
	public Administrador() {}
	
	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	
	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	
	/**
	 * Informa la cantidad de meses que este administrador lleva ocupando ese rol.
	 * @return La cantidad de meses.
	 */
	public long mesesSiendoAdministradorHasta(LocalDate fechaHasta)
	{
		return Period.between(this.fechaAlta, fechaHasta).toTotalMonths();
	}
}
