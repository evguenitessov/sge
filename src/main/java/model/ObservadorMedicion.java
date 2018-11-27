package model;

public interface ObservadorMedicion {
	
	public void notificarMedicion(double medicion);

	public void suscribirseASensor();

}
