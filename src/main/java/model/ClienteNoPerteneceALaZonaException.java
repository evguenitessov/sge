package model;

/**
 * Cuando una Zona intenta hacer una operación sobre un Cliente que no le pertenece
 */
@SuppressWarnings("serial")
public class ClienteNoPerteneceALaZonaException extends RuntimeException {

    public ClienteNoPerteneceALaZonaException(String message) {
        super(message);
    }
}
