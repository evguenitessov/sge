package model;

import javax.persistence.Embeddable;

import org.geotools.referencing.GeodeticCalculator;

@Embeddable
public class Posicion implements Localizable {
    private double latitud;
    private double longitud;
    
    public Posicion() {
		this.latitud = 0;
		this.longitud = 0;
	}

    public Posicion(double latitud, double longitud) {
        this.setLatitud(latitud);
        this.setLongitud(longitud);
	}
	
	/**
     * Devuelve la distancia en kilómetros hacia posición.
     */
    public double distanciaHasta(Localizable otraPosicion) {
        GeodeticCalculator calculadora = new GeodeticCalculator();
        calculadora.setStartingGeographicPoint(this.longitud, this.latitud);
        calculadora.setDestinationGeographicPoint(
			otraPosicion.getPosicion().getLongitud(),
			otraPosicion.getPosicion().getLatitud()
		);

        return calculadora.getOrthodromicDistance() / 1000;
	}

	public Posicion getPosicion() {
		return this;
	}

	/**
	 * @return the longitud
	 */
	public double getLongitud() {
		return longitud;
	}

	/**
	 * @param longitud the longitud to set
	 */
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	/**
	 * @return the latitud
	 */
	public double getLatitud() {
		return latitud;
	}

	/**
	 * @param latitud the latitud to set
	 */
	public void setLatitud(double latitud) {
		this.latitud = latitud;
    }
}
