package model;

public enum Puntos {
	porRegistrarDispositivoInteligente(15),
	porConvertirADispositivoInteligente(10);
	
	private int puntos;
	
	private Puntos(int puntos) {
		this.puntos = puntos;
	}

	public int getPuntos() {
		return puntos;
	}
}
