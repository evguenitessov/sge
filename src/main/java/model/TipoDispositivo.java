package model;

public enum TipoDispositivo {
    AireAcondicionado(90, 360, true),
    Lampara(90, 360, true),
    Televisor(90, 360, true),
    Lavarropas(6, 30, true),
    Computadora(60, 360, true),
    Microondas(3, 15, true),
    Plancha(3, 30, true),
    Ventilador(120, 360, true),
    Heladera(0, 0, false);

    private double usoMinimo;
    private double usoMaximo;
    private boolean puedeSerDesconectado;

    private TipoDispositivo(double usoMinimo, double usoMaximo, boolean puedeSerDesconectado) {
        this.usoMinimo = usoMinimo;
        this.usoMaximo = usoMaximo;
        this.puedeSerDesconectado = puedeSerDesconectado;
    }
    
    @Override
    public String toString() {
      switch(this) {
        case AireAcondicionado: return "Aire Acondicionado";
        case Lampara: return "Lampara";
        case Televisor: return "Televisor";
        case Lavarropas: return "Lavarropas";
        case Computadora: return "Computadora";
        case Microondas: return "Microondas";
        case Plancha: return "Plancha";
        case Ventilador: return "Ventilador";
        case Heladera: return "Heladera";
        default: throw new IllegalArgumentException();
      }
    }

	public boolean puedeSerDesconectado() {
		return this.puedeSerDesconectado;
	}

	public double getUsoMinimo() {
		return this.usoMinimo;
    }

    public double getUsoMaximo() {
		return this.usoMaximo;
    }
}