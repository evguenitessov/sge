package model;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import org.geotools.geometry.jts.GeometryBuilder;

/**
 * Permite hacer operaciones sobre un círculo proyectado sobre la Tierra
 */
public class Circulo {
    public static double RadioDeLaTierra = 6378.1370;
    public static double ElOtroRadioDeLaTierra = 6356.7523142;
    private Posicion posicion;
    private double radio;

    public Circulo(double latitud, double longitud, double radio) {
        this.posicion = new Posicion(latitud, longitud);
        this.radio = radio;
    }

    public Circulo(Posicion posicion, double radio) {
        this.posicion = posicion;
        this.radio = radio;
    }

    /**
     * Indica si este Circulo contiene un punto.
     */
    public boolean contieneA(Localizable unaPosicion) {
        GeometryBuilder gb = new GeometryBuilder();
		Polygon circulo = gb.circle(
			this.posicion.getLatitud(),
            this.posicion.getLongitud(),
            this.radioEnGrados(),
			100
		);
		Point punto = gb.point(
			unaPosicion.getPosicion().getLatitud(),
			unaPosicion.getPosicion().getLongitud()
		);

		return circulo.intersects(punto);
    }

    /**
     * El radio tenemos que tenerlo expresado de esta manera: como el radio (en KM) dividido el tamaño de longitud.
     */
    public double radioEnGrados() {
        return this.radio / this.tamanioDeUnGradoDeLongitud();
    }

    /**
     * El tamaño de un grado de longitud va variando según la latitud (N/S).
     */
    public double tamanioDeUnGradoDeLongitud() {
        double latitudEnRadianes = this.posicion.getLatitud() * Math.PI / 180;

        double excentricidad = ((Math.pow(Circulo.RadioDeLaTierra, 2)) - (Math.pow(Circulo.ElOtroRadioDeLaTierra, 2)))
            / (Math.pow(Circulo.RadioDeLaTierra, 2));

        return Math.PI * Circulo.RadioDeLaTierra * Math.cos(latitudEnRadianes)
            / (180 * Math.sqrt(1 - (Math.pow(excentricidad, 2) * (Math.pow(Math.sin(latitudEnRadianes), 2)))));
    }
}
