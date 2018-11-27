package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Transformador implements Localizable {
	
	@Id
	@GeneratedValue	
	private Long id;
	
	@Embedded
	private Posicion posicion;
	
	@OneToMany(mappedBy="transformador")	
	private List<Cliente> clientes;
	
	public Transformador() {
		this.posicion = new Posicion();
		this.clientes = new ArrayList<>();
	}

    public Transformador(Posicion posicion) {
		this.setPosicion(posicion);
		this.clientes = new ArrayList<>();
	}

	public Long getId() {
		return this.id;
	}
	
	public double distanciaHasta(Localizable otroLugar) {
		return this.posicion.distanciaHasta(otroLugar);
	}

	/**
	 * @return the clientes
	 */
	public List<Cliente> getClientes() {
		return clientes;
	}

	/**
	 * @param clientes the clientes to set
	 */
	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	/**
	 * @return the posicion
	 */
	public Posicion getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}

	/**
	 * La energía que está suministrando este transformador.
	 */
	public double energiaSuministrada() {
		return this.clientes
			.stream()
			.mapToDouble(cliente -> cliente.consumoActual())
			.sum();
	}

	/**
	 * Devuelve la energía suministrada por este transformador en un período de tiempo
	 * @return
	 */
	public double energiaEntre(LocalDateTime desde, LocalDateTime hasta) {
		return this.clientes
			.stream()
			.mapToDouble(c -> c.consumoPorPeriodo(desde, hasta))
			.sum();
	}
}
