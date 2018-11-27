package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.uqbarproject.jpa.java8.extras.convert.LocalDateConverter;

@Entity
@DiscriminatorValue("1")
public class Cliente extends Usuario implements Localizable {
	
	private String nombre;
	private String apellido;
	private String tipoDocumento;
	private String numeroDocumento;
	private String telefono;
	private String domicilio;
	
	@Convert(converter=LocalDateConverter.class)
	private LocalDate fechaAlta;
	
	@Enumerated
	private Categoria categoria;
	@OneToMany(mappedBy="cliente")
	private List<Dispositivo> dispositivos;
	
	private int puntos;
	
	@Embedded
	private Posicion posicion;
	
	@ManyToOne
	private Transformador transformador;
	
	@OneToMany(mappedBy="cliente")
	private List<ObservadorDispositivos> observadoresDispositivos;

	public Cliente(String nombre, String apellido, String tipoDocumento, String numeroDocumento,
		String telefono, String domicilio, LocalDate fechaAlta) {
	    this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.fechaAlta = fechaAlta;
    	this.dispositivos = new ArrayList<>();
    	this.observadoresDispositivos = new ArrayList<>();
		this.puntos = 0;
		this.posicion = new Posicion();
    }
	
	public Cliente() {
		this.dispositivos = new ArrayList<>();
		this.observadoresDispositivos = new ArrayList<>();
		this.posicion = new Posicion();
	}
	
	@Override
	public boolean esAdmin() {
		return false;
	}
	

	/**
	 * @return the transformador
	 */
	public Transformador getTransformador() {
		return transformador;
	}

	/**
	 * @param transformador the transformador to set
	 */
	public void setTransformador(Transformador transformador) {
		this.transformador = transformador;
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

	public int getPuntos() {
		return puntos;
	}
	
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public Categoria getCategoria() {
		return categoria;
	}
	
	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	
	public List<Dispositivo> getDispositivos() {
		return dispositivos;
	}

	/**
	 * Metodo que solo debe ser utilizado por la clase Categoria
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	/**
	 * Indica si al menos un dispositivo está encendido.
	 * @return Si tiene un dispositivo encendido.
	 */
	public boolean tieneDispositivoEncendido() {
		return this
			.dispositivosQueSeComunicanConElSistema()
			.anyMatch(dispositivo -> dispositivo.estaEncendido());
	}
	
	/**
	 * Devuelve la cantidad de dispositivos que tiene este cliente.
	 * @return La cantidad de dispositivos de este cliente.
	 */
	public long cantidadDispositivos() {
		return this.dispositivos.stream().count();
	}
	
	/**
	 * Devuelve la cantidad de dispositivos encendidos que tiene este cliente.
	 * @return La cantidad de dispositivos encendidos de este cliente.
	 */
	public long cantidadDispositivosEncendidos() {
		return this
			.dispositivosQueSeComunicanConElSistema()
			.filter(dispositivo -> dispositivo.estaEncendido())
			.count();
	}
	
	/**
	 * Devuelve la cantidad de dispositivos apagados que tiene este cliente.
	 * @return La cantidad de dispositivos apagados de este cliente.
	 */
	public long cantidadDispositivosApagados() {
		return this
			.dispositivosQueSeComunicanConElSistema()
			.filter(dispositivo -> !dispositivo.estaEncendido())
			.count();
	}
	
	/**
	 * Devuelve el consumo mensual (del último período) de este cliente.
	 * @return El consumo mensual (del último período) de este cliente.
	 */
	public double consumo()
	{
		return this
			.getDispositivos()
			.stream()
			.mapToDouble(dispositivo -> dispositivo.consumoUltimoPeriodo())
			.sum();
	}

	public Stream<Dispositivo> dispositivosQueSeComunicanConElSistema() {
		return this
			.getDispositivos()
			.stream()
			.filter(dispositivo -> dispositivo.puedeComunicarseConElSistema());
	}

	/**
	 * Devuelve la sumatoria del consumo por hora de los dispositivos que están actualmente encendidos.
	 */
	public double consumoActual() {
		return this
			.dispositivosQueSeComunicanConElSistema()
			.filter(dispositivo -> dispositivo.estaEncendido())
			.mapToDouble(dispositivo -> dispositivo.getKWHora())
			.sum();
	}
	
	public void sumarPuntos(int puntos)
	{
		this.setPuntos(this.getPuntos() + puntos);
	}
	
	public void adaptarDispositivo(Dispositivo dispositivo)
	{
		dispositivo.convertirAInteligente();
		this.observadoresDispositivos.forEach(observador -> observador.notificarConversionDispositivo(this));
	}
	
	public void registrarDispositivo(Dispositivo dispositivo) 
	{
		this.dispositivos.add(dispositivo);
		this.observadoresDispositivos.forEach(observador -> observador.notificarRegistracionDispositivo(this, dispositivo));
	}

	public void conectarseA(Transformador unTransformador) {
		unTransformador.getClientes().add(this);
		this.setTransformador(unTransformador);
	}

	public double distanciaHasta(Localizable otroLugar) {
		return this.posicion.distanciaHasta(otroLugar);
	}

	/**
	 * Devuelve un dispositivo de este cliente con el noombre de dispositivo especificado.
	 */
	public Dispositivo buscarDispositivoPorNombre(String nombreDispositivo) {
		return this.getDispositivos()
			.stream()
			.filter(d -> d.getNombre().equals(nombreDispositivo))
			.findFirst()
			.get();
	}
	
	/**
	 * Devuelve el consumo de este cliente entre dos fechas.
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return
	 */
	public double consumoPorPeriodo(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		return this
			.dispositivosQueSeComunicanConElSistema()			
			.mapToDouble(dispositivo -> dispositivo.consumoEntre(fechaDesde, fechaHasta))
			.sum();
	}

	/**
	 * Todas las mediciones de este Cliente
	 */
	public List<Intervalo> mediciones() {
		List<Intervalo> mediciones = this.dispositivosQueSeComunicanConElSistema()
			.flatMap(d -> d.getIntervalos().stream())
			.collect(Collectors.toList());
		mediciones.sort((i1, i2) -> i2.getInicio().compareTo(i1.getInicio()));

		return mediciones;	
	}
}
