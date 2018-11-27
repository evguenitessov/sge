package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Zona implements Localizable {
    private List<Transformador> transformadores;
    private Posicion posicion;
    private double radioDeCobertura;
    private String descripcion;

    public Zona(String descripcion, Posicion posicion, double radioDeCobertura) {
        this.setDescripcion(descripcion);
        this.setPosicion(posicion);
        this.setRadioDeCobertura(radioDeCobertura);
        this.transformadores = new ArrayList<>();
	}
	
	public double distanciaHasta(Localizable otroLugar) {
		return this.posicion.distanciaHasta(otroLugar);
	}

	/**
	 * Consumo total de energía de esta zona.
	 */
	public double consumoTotal() {
		return this.transformadores
			.stream()
			.mapToDouble(transformador -> transformador.energiaSuministrada())
			.sum();
	}

	/**
	 * Indica si esta Zona contiene a un Cliente en su radio de cobertura.
	 */
	public boolean contieneA(Cliente unCliente) {
		return new Circulo(this.posicion, this.radioDeCobertura).contieneA(unCliente.getPosicion());
	}

	/**
	 * Devuelve una lista con los clientes que pertenecen únicamente a esta zona.
	 */
	public List<Cliente> filtrarClientesDeEstaZona(List<Cliente> clientes) {
		return clientes
			.stream()
			.filter(c -> this.contieneA(c))
			.collect(Collectors.toList());
	}

	/**
	 * Conecta a un cliente de esta Zona al Transformador más cercano.
	 * @throws ClienteNoPerteneceALaZonaException
	 */
	public void conectarAlTransformadorMasCercano(Cliente unCliente) throws ClienteNoPerteneceALaZonaException {
		if (!this.contieneA(unCliente)) {
			throw new ClienteNoPerteneceALaZonaException("Se intentó conectar a un Cliente que no pertenece a la Zona");
		}
		Transformador transformadorMasCercano = this.transformadores
			.stream()
			.min(Comparator.comparingDouble(unCliente::distanciaHasta))
			.get();

		unCliente.conectarseA(transformadorMasCercano);
	}

	/**
	 * Indica si un Cliente está conectado a un Transformador
	 */
	public boolean estaConectadoATransformador(Cliente unCliente) {
		return this.transformadores
			.stream()
			.anyMatch(t -> t.equals(unCliente.getTransformador()));
	}

	/**
	 * Devuelve el Transformador al que está conectado un Cliente.
	 */
	public Transformador transformadorDe(Cliente unCliente) {
		return this.transformadores
			.stream()
			.filter(t -> t.equals(unCliente.getTransformador()))
			.findFirst()
			.get();
	}

	/**
	 * Dada una lista de clientes, conecta a los transformadores más cercanos de aquellos clientes
	 * que pertenecen a esta Zona.
	 */
	public void conectarClientesDeLaZona(List<Cliente> clientes) {
		this.filtrarClientesDeEstaZona(clientes)
			.forEach(cliente -> this.conectarAlTransformadorMasCercano(cliente));
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the transformadores
	 */
	public List<Transformador> getTransformadores() {
		return transformadores;
	}

	/**
	 * @param transformadores the transformadores to set
	 */
	public void setTransformadores(List<Transformador> transformadores) {
		this.transformadores = transformadores;
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
	 * @return the radioDeCobertura
	 */
	public double getRadioDeCobertura() {
		return radioDeCobertura;
	}

	/**
	 * @param radioDeCobertura the radioDeCobertura to set
	 */
	public void setRadioDeCobertura(double radioDeCobertura) {
		this.radioDeCobertura = radioDeCobertura;
	}
}
