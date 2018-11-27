package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import model.Dispositivo;
import model.Estado;
import model.TipoDispositivo;
import model.Modulo;

public class DispositivoTest {
	
	Dispositivo dispositivoInteligente;
	Dispositivo dispositivoEstandar;
	
	@Before
	public void setUp() {	
		dispositivoInteligente = new Dispositivo("Computadora",100, Modulo.Inteligente, TipoDispositivo.AireAcondicionado);
		dispositivoEstandar = new Dispositivo("Atari", 200, Modulo.Estandar, TipoDispositivo.AireAcondicionado);
	}

	@Test
	public void convertirAInteligente_esEstandar_seConvierteAInteligente() {
		// Act
		dispositivoEstandar.convertirAInteligente();
		
		// Assert
		assertEquals(Modulo.Inteligente, dispositivoEstandar.getModulo());
	}
	
	@Test
	public void puedeComunicarseConElSistema_esEstandar_devuelveFalse() {
		// Arrange
		boolean esperado = false;
		
		// Act
		boolean obtenido = dispositivoEstandar.puedeComunicarseConElSistema();
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void puedeComunicarseConElSistema_esInteligente_devuelveTrue() {
		// Arrange
		boolean esperado = true;
		
		// Act
		boolean obtenido = dispositivoInteligente.puedeComunicarseConElSistema();
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void encender_esInteligenteYEstaApagado_loEnciende() {
		// Arrange
		dispositivoInteligente.apagar(LocalDateTime.now());
		boolean estabaEncendido = dispositivoInteligente.estaEncendido();
		
		// Act
		dispositivoInteligente.encender(LocalDateTime.now());
		
		// Assert
		assertFalse(estabaEncendido);
		assertTrue(dispositivoInteligente.estaEncendido());
	}
	
	@Test
	public void apagar_esInteligenteYYaEstabaEncendido_LoApaga() {
		// Arrange
		dispositivoInteligente.encender(LocalDateTime.now());
		boolean estabaApagado = dispositivoInteligente.estaApagado();
		
		// Act
		dispositivoInteligente.apagar(LocalDateTime.now());
		
		// Assert
		assertFalse(estabaApagado);
		assertTrue(dispositivoInteligente.estaApagado());
	}
	
	@Test
	public void ahorroDeEnergia_esInteligenteYEstabaEncendido_pasaAAhorroDeEnergia() {
		dispositivoInteligente.ahorroDeEnergia(LocalDateTime.now());

		assertEquals(dispositivoInteligente.getEstado(), Estado.AhorroDeEnergia);
	}
	
	@Test
	public void encender_esInteligenteYEstabaAhorroDeEnergia_pasaAEncendido() {
		dispositivoInteligente.encender(LocalDateTime.now());

		assertEquals(dispositivoInteligente.getEstado(), Estado.Encendido);
	}
	
	@Test
	public void consumoEntre_seLePasanDosFechas_devuelveElConsumoEnEseIntervalo() {
		// Arrange
		double consumoEsperado = 50;
		dispositivoInteligente.encender(LocalDateTime.parse("1991-10-14T10:00:00"));
		dispositivoInteligente.apagar(LocalDateTime.parse("1991-10-14T10:30:00"));
		
		// Act
		double consumoObtenido = dispositivoInteligente.consumoEntre(
			LocalDateTime.parse("1991-10-14T10:00:00"),
			LocalDateTime.parse("1991-10-14T10:30:00")
		);
		
		// Assert
		assertEquals(consumoEsperado, consumoObtenido, 0);
	}
	
	@Test
	public void consumoDiario_tiene200kwh_Devuelve4800kwh() {
		// Arrange
		double consumoEsperado = 4800;
		
		// Act
		double consumoObtenido = dispositivoEstandar.consumoDiario();
		
		// Assert
		assertEquals(consumoEsperado, consumoObtenido, 0);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void encender_esEstandar_devuelveExcepcion() {
		// Act
		dispositivoEstandar.encender(LocalDateTime.now());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void apagar_esEstandar_devuelveExcepcion() {
		// Act
		dispositivoEstandar.apagar(LocalDateTime.now());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void ahorroDeEnergia_esEstandar_devuelveExcepcion() {
		// Act
		dispositivoEstandar.ahorroDeEnergia(LocalDateTime.now());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void estaApagado_esEstandar_devuelveExcepcion() {
		// Act
		dispositivoEstandar.estaApagado();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void estaEncendido_esEstandar_devuelveExcepcion() {
		// Act
		dispositivoEstandar.estaEncendido();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void consumoEntre_esEstandar_devuelveExcepcion() {
		// Act
		dispositivoEstandar.consumoEntre(LocalDateTime.now(), LocalDateTime.now());
	}

	@Test
	public void consumoPromedioEntre_gasto200kWhYElPeriodoEs10Horas_devuelve20kWh() {
		// Estuvo encendido 2 horas en total.
		dispositivoInteligente.encender(LocalDateTime.parse("1991-10-14T10:00:00"));
		dispositivoInteligente.apagar(LocalDateTime.parse("1991-10-14T11:00:00"));
		dispositivoInteligente.encender(LocalDateTime.parse("1991-10-14T15:00:00"));
		dispositivoInteligente.apagar(LocalDateTime.parse("1991-10-14T16:00:00"));

		double promedioObtenido = dispositivoInteligente.consumoPromedioEntre(
			LocalDateTime.parse("1991-10-14T08:00:00"),
			LocalDateTime.parse("1991-10-14T18:00:00")
		);

		assertEquals(20, promedioObtenido, 0.1);
	}
}
