package model;

/**
 * Define un tipo de objeto que puede ser localizado en un mapa.
 */
public interface Localizable {

    public double distanciaHasta(Localizable otroLugar);
    public Posicion getPosicion();
}
