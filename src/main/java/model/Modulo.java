package model;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * Define si el comportamiento de un dispositivo es inteligente o estándar.
 */
public enum Modulo {
	Inteligente () {        	
		
		@Override
		public boolean estaApagado(Dispositivo dispositivo) {
			return !dispositivo.getEstado().estaEncendido();
		}
		
		@Override
		public boolean estaEncendido(Dispositivo dispositivo) {
			return dispositivo.getEstado().estaEncendido();
		}
		
		@Override
		public double consumoEntre(Dispositivo dispositivo, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
			return intervalosEntre(dispositivo, fechaDesde, fechaHasta)
				.mapToDouble(Intervalo::duracionTotal)
				.sum() * dispositivo.getKWHora();
		}

		@Override
		public Stream<Intervalo> intervalosEntre(Dispositivo dispositivo, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
			return dispositivo
				.getIntervalos()
				.stream()
				.filter(Intervalo::finalizo)
				.filter(i -> i.estaEntre(fechaDesde, fechaHasta));
		}
		
		@Override
		public void apagar(Dispositivo dispositivo, LocalDateTime momento) {
			dispositivo.getEstado().apagar(dispositivo, momento);
		}
		
		@Override
		public void encender(Dispositivo dispositivo, LocalDateTime momento) {
			dispositivo.getEstado().encender(dispositivo, momento);
		}

		@Override
		public void ahorroDeEnergia(Dispositivo dispositivo, LocalDateTime momento) {
			dispositivo.getEstado().ahorroDeEnergia(dispositivo, momento);
		}

		@Override
		public boolean puedeComunicarseConElSistema() {
			return true;
		}

		@Override
		public double consumoDiario(Dispositivo dispositivo) {
			return dispositivo.getKWHora() * 24;
		}

		@Override
		public double consumoUltimoPeriodo(Dispositivo dispositivo) {
			return dispositivo.consumoEntre(
				LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0), // Inicio del mes pasado
				LocalDateTime.now().withDayOfMonth(1).minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0) // Último día del mes pasado
			);
		}
		
		@Override
		public void convertirAInteligente(Dispositivo dispositivo) {
			// No hacemos nada, ya es un dispositivo inteligente.
		}
	},        
	Estandar (){
		@Override
		public boolean estaApagado(Dispositivo dispositivo) {
			throw new UnsupportedOperationException("Este Dispositivo no puede comunicarse con el sistema");
		}

		@Override
		public boolean estaEncendido(Dispositivo dispositivo) {
			throw new UnsupportedOperationException("Este Dispositivo no puede comunicarse con el sistema");
		}

		@Override
		public double consumoEntre(Dispositivo dispositivo, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
			throw new UnsupportedOperationException("Este Dispositivo no puede comunicarse con el sistema");
		}

		@Override
		public void apagar(Dispositivo dispositivo, LocalDateTime momento) {
			throw new UnsupportedOperationException("Este Dispositivo no puede comunicarse con el sistema");
			
		}

		@Override
		public void encender(Dispositivo dispositivo, LocalDateTime momento) {
			throw new UnsupportedOperationException("Este Dispositivo no puede comunicarse con el sistema");
		}

		@Override
		public void ahorroDeEnergia(Dispositivo dispositivo, LocalDateTime momento) {
			throw new UnsupportedOperationException("Este Dispositivo no puede comunicarse con el sistema");
		}

		@Override
		public boolean puedeComunicarseConElSistema() {
			return false;
		}

		@Override
		public double consumoDiario(Dispositivo dispositivo) {
			return dispositivo.getKWHora() * 24;
		}

		@Override
		public double consumoUltimoPeriodo(Dispositivo dispositivo) {
			return dispositivo.consumoEntre(LocalDateTime.now().withDayOfMonth(1), LocalDateTime.now());
		}
		
		@Override
		public void convertirAInteligente(Dispositivo dispositivo) {
			dispositivo.setModulo(Inteligente);
		}

		@Override
		public Stream<Intervalo> intervalosEntre(Dispositivo dispositivo, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
			throw new UnsupportedOperationException("Este Dispositivo no puede comunicarse con el sistema");
		}
	};
	
	 @Override
    public String toString() {
      switch(this) {
        case Inteligente: return "Inteligente";
        case Estandar: return "Estandar";        
        default: throw new IllegalArgumentException();
      }
    }

	private Modulo() {
	}
	
	// Firmas de las funciones que debe tener un módulo
	public abstract boolean estaApagado(Dispositivo dispositivo);
	public abstract boolean estaEncendido(Dispositivo dispositivo);
	public abstract double consumoEntre(Dispositivo dispositivo, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
	public abstract void apagar(Dispositivo dispositivo, LocalDateTime momento);
	public abstract void encender(Dispositivo dispositivo, LocalDateTime momento);
	public abstract void ahorroDeEnergia(Dispositivo dispositivo, LocalDateTime momento);
	public abstract boolean puedeComunicarseConElSistema();
	public abstract double consumoDiario(Dispositivo dispositivo);
	/**
	 * Devuelve el consumo del mes actual.
	 * @param dispositivo
	 * @return
	 */
	public abstract double consumoUltimoPeriodo(Dispositivo dispositivo);
	public abstract void convertirAInteligente(Dispositivo dispositivo);
	public abstract Stream<Intervalo> intervalosEntre(Dispositivo dispositivo, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}
