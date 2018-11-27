package model;

import java.util.function.DoubleFunction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Regla implements ObservadorMedicion{
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne(cascade={CascadeType.PERSIST})
	private Sensor sensor;
	
	@OneToOne(cascade={CascadeType.PERSIST})
	private Actuador actuador;
	
	@Transient
	private DoubleFunction<Boolean> condicion;
	
	public Regla() {}
	
	public Long getId() {
		return id;
	}
	
	public Regla(Sensor sensor, Actuador actuador, DoubleFunction<Boolean> condicion) {
	    this.sensor = sensor;
        this.actuador = actuador;
        this.condicion = condicion;
    }
	
	@Override
	public void suscribirseASensor() {
		this.sensor.suscribir(this);
	}
	
	@Override
	public void notificarMedicion(double medicion) {
		if(this.condicion.apply(medicion))
			actuador.ejecutar();
	}
}
