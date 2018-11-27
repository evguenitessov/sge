package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Dispositivo {
	@Id
	@GeneratedValue	
	private Long id;
	
	private String nombre;
	private double kWHora;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dispositivo")
	private List<Intervalo> intervalos;
	
	@Enumerated
	private Modulo modulo;

	@Enumerated
	private Estado estado;
		
	@Enumerated
	private TipoDispositivo tipoDispositivo;

	@ManyToOne
	private Cliente cliente;
	
	public Dispositivo() {}
	
	public Long getId() {
		return id;
	}

	public Dispositivo(String nombre, double kWHora, Modulo modulo, TipoDispositivo tipo) {
        this.nombre = nombre;
        this.kWHora = kWHora;
		this.modulo = modulo;
		this.tipoDispositivo = tipo;
		this.intervalos = new ArrayList<>();
		this.setEstado(Estado.Apagado);
	}

	public TipoDispositivo getTipoDispositivo() {
		return this.tipoDispositivo;
	}

	public void setTipoDispositivo(TipoDispositivo unTipo) {
		this.tipoDispositivo = unTipo;
	}
	
	public Modulo getModulo()
	{
		return this.modulo;
	}
	
	public void convertirAInteligente() {
		this.modulo.convertirAInteligente(this);
	}
	
	public boolean puedeComunicarseConElSistema() {
		return this.modulo.puedeComunicarseConElSistema();
	}
	
	public boolean estaApagado() {
		return this.modulo.estaApagado(this);
	}
	
	/**
	 * Devuelve el consumo del mes actual.
	 */
	public double consumoUltimoPeriodo() {
		return this.modulo.consumoUltimoPeriodo(this);
	}
	
	/**
	 * Devuelve el consumo entre dos fechas especificadas.
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return El consumo en kwh
	 */
	public double consumoEntre(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		return this.modulo.consumoEntre(this, fechaDesde, fechaHasta);
	}

	public Stream<Intervalo> intervalosEntre(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		return this.modulo.intervalosEntre(this, fechaDesde, fechaHasta);
	}
	
	public void apagar(LocalDateTime momento) {
		this.modulo.apagar(this, momento);
	}
	
	public void encender(LocalDateTime momento) {
		this.modulo.encender(this, momento);
	}
	
	public void ahorroDeEnergia(LocalDateTime momento) {
		this.modulo.ahorroDeEnergia(this, momento);
	}	
	
	/**
	 * Devuelve el consumo diario de este dispositivo si estuviese encendido todo el día.
	 */
	public double consumoDiario() {
		return this.modulo.consumoDiario(this);
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getKWHora() {
		return kWHora;
	}
	
	public void setKWHora(double kWHora) {
		this.kWHora = kWHora;
	}

	public boolean estaEncendido() {
		return this.modulo.estaEncendido(this);
	}
	
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public double getUsoMaximo() {
		return this.getTipoDispositivo().getUsoMaximo();
	}

	public double getUsoMinimo() {
		return this.getTipoDispositivo().getUsoMinimo();
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Intervalo> getIntervalos() {
		return this.intervalos;
	}

	public void setIntervalos(List<Intervalo> intervalos) {
		this.intervalos = intervalos;
	}

	public Intervalo ultimoIntervalo() {
		return this.intervalos.get(this.intervalos.size() - 1);
	}

	/**
	 * Devuelve el consumo promedio por hora entre dos fechas.
	 * @param desde
	 * @param hasta
	 * @return
	 */
	public double consumoPromedioEntre(LocalDateTime desde, LocalDateTime hasta) {
		return this.consumoEntre(desde, hasta) / (new Intervalo(desde, hasta, Estado.Encendido)).duracionTotal();
	}
}
