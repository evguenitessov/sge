package model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DispositivoGenerico {
	@Id
	@GeneratedValue	
	private Long id;
	private String nombre;
	private double kWHora;
	@Enumerated
	private Modulo modulo;
	@Enumerated
	private TipoDispositivo tipoDispositivo;
	
	public DispositivoGenerico() {}

	public DispositivoGenerico(String nombre, double kWHora, Modulo modulo, TipoDispositivo tipo) {
        this.nombre = nombre;
        this.kWHora = kWHora;
        this.modulo = modulo;
        this.tipoDispositivo = tipo;
    }

    public Long getId() {
		return id;
	}
    
    /**
     * @return the kWHora
     */
    public double getKWHora() {
        return kWHora;
    }

    /**
     * @param kWHora the kWHora to set
     */
    public void setKWHora(double kWHora) {
        this.kWHora = kWHora;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the modulo
     */
    public Modulo getModulo() {
        return modulo;
    }

    /**
     * @param Modulo the modulo to set
     */
    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    /**
     * @return the tipoDispositivo
     */
    public TipoDispositivo getTipoDispositivo() {
        return tipoDispositivo;
    }

    /**
     * @param TipoDispositivo the tipoDispositivo to set
     */
    public void setTipoDispositivo(TipoDispositivo tipoDispositivo) {
        this.tipoDispositivo = tipoDispositivo;
    }

    /**
     * Construye un Dispositivo con la información de este dispositivo genérico
     */
    public Dispositivo construir() {
        return new Dispositivo(
            this.nombre,
            this.kWHora,
            this.modulo,
            this.tipoDispositivo
        );
    }
}
