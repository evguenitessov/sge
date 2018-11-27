package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Actuador {
	
	@Id
	@GeneratedValue
	private Long id;
	
	public Actuador() {}
	
	public abstract void ejecutar();

}
