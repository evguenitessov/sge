package model;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
public abstract class ObservadorDispositivos 
{
	@Id
	@GeneratedValue	
	private Long id;
	@ManyToOne
	private Cliente cliente;
	
	public ObservadorDispositivos() {		
	}
	
	public Long getId() {
		return id;
	}
	
	public void notificarRegistracionDispositivo(Cliente cliente, Dispositivo dispositivo) {}
	public void notificarConversionDispositivo(Cliente cliente) {}
}
