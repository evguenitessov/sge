package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Sensor {
	
	@Id
	@GeneratedValue
	private Long id;
	
	public Sensor() {}
	
	public abstract double medir();
	
	public abstract void suscribir(ObservadorMedicion observador);

}
