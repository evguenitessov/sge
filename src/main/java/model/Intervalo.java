package model;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Representa un intervalo de tiempo con un momento de inicio y uno momento de fin
 */
@Entity
public class Intervalo {
    @Id
	@GeneratedValue	
	private Long id;
    @Convert(converter=LocalDateTimeConverterFix.class)
    private LocalDateTime inicio;
    @Convert(converter=LocalDateTimeConverterFix.class)
    private LocalDateTime fin;
    private Estado estado;
    @ManyToOne
    private Dispositivo dispositivo;

    public Intervalo (LocalDateTime inicio) {
        this.setInicio(inicio);
    }
    
    /**
     * @return the estado
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Intervalo() {
    }

    public Intervalo(LocalDateTime inicio, Estado estado) {
        this.setInicio(inicio);
        this.setEstado(estado);
    }

    public Intervalo(LocalDateTime inicio, LocalDateTime fin, Estado estado) {
        this.setInicio(inicio);
        this.setFin(fin);
        this.setEstado(estado);
    }

    /**
     * Obtiene la duración de este intervalo en horas.
     */
    public double duracionTotal() {
        return ((double) Duration.between(this.inicio, this.fin).getSeconds()) /  ((double) 3600);
    }
    
    /**
     * @return the fin
     */
    public LocalDateTime getFin() {
        return fin;
    }

    /**
     * @param fin the fin to set
     */
    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    /**
     * @return the inicio
     */
    public LocalDateTime getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    /**
     * Indica si este intervalo tiene un fin.
     */
    public boolean finalizo() {
        return this.fin != null;
    }

    public boolean estaEntre(LocalDateTime inicio, LocalDateTime fin) {
        return (this.inicio.isAfter(inicio) || this.inicio.isEqual(inicio))
            && (this.fin.isBefore(fin) || this.fin.isEqual(fin));
    }

    public Dispositivo getDispositivo() {
        return this.dispositivo;
    }
}
