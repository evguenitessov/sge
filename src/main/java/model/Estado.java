package model;

import java.time.LocalDateTime;

/**
 * Representa el estado en que se encuentra un dispositivo puede ser Encendido, Apagado o Ahorro de Energía.
 */
public enum Estado {
    Encendido() {
        @Override
        public void encender(Dispositivo dispositivo, LocalDateTime momento) {
            // No hacemos nada.
        }

        @Override
        public void apagar(Dispositivo dispositivo, LocalDateTime momento) {
            dispositivo.setEstado(Apagado);
            dispositivo.ultimoIntervalo().setFin(momento);
        }

        @Override
        public void ahorroDeEnergia(Dispositivo dispositivo, LocalDateTime momento) {
            dispositivo.setEstado(AhorroDeEnergia);
            dispositivo.ultimoIntervalo().setFin(momento);
            dispositivo.getIntervalos().add(new Intervalo(momento, AhorroDeEnergia));
        }

        @Override
        public boolean estaEncendido() {
            return true;
        }
    },
    Apagado() {
        @Override
        public void encender(Dispositivo dispositivo, LocalDateTime momento) {
            dispositivo.setEstado(Encendido);
            dispositivo.getIntervalos().add(new Intervalo(momento, Encendido));
        }

        @Override
        public void apagar(Dispositivo dispositivo, LocalDateTime momento) {
            // No hacemos nada.
        }

        @Override
        public void ahorroDeEnergia(Dispositivo dispositivo, LocalDateTime momento) {
            dispositivo.setEstado(AhorroDeEnergia);
            dispositivo.getIntervalos().add(new Intervalo(momento, AhorroDeEnergia));
        }

        @Override
        public boolean estaEncendido() {
            return false;
        }
    },
    AhorroDeEnergia() {
        @Override
        public void encender(Dispositivo dispositivo, LocalDateTime momento) {
            dispositivo.setEstado(Encendido);
            dispositivo.ultimoIntervalo().setFin(momento);
            dispositivo.getIntervalos().add(new Intervalo(momento, Encendido));
        }

        @Override
        public void apagar(Dispositivo dispositivo, LocalDateTime momento) {
            dispositivo.setEstado(Apagado);
            dispositivo.ultimoIntervalo().setFin(momento);
        }

        @Override
        public void ahorroDeEnergia(Dispositivo dispositivo, LocalDateTime momento) {
            // No hacemos nada.
        }

        @Override
        public boolean estaEncendido() {
            return true;
        }
    };

    private Estado() {
    }

    // Firmas de funciones que deben ser implementadas
    public abstract void encender(Dispositivo dispositivo, LocalDateTime momento);
	public abstract void apagar(Dispositivo dispositivo, LocalDateTime momento);
	public abstract void ahorroDeEnergia(Dispositivo dispositivo, LocalDateTime momento);
	public abstract boolean estaEncendido();
}
