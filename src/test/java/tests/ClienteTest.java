package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import model.Cliente;
import model.Dispositivo;
import model.Modulo;
import model.TipoDispositivo;

public class ClienteTest {
	
	Cliente cliente;
	
	Dispositivo dispositivoEncendido;
	Dispositivo dispositivoApagado1;
	Dispositivo dispositivoApagado2;
	
	@Before
	public void setUp() {	
		cliente = new Cliente(null, null, null, null, null, null, null);
		
		dispositivoEncendido = new Dispositivo(null,100,null, TipoDispositivo.AireAcondicionado);
		dispositivoApagado1 = new Dispositivo(null,50,null, TipoDispositivo.AireAcondicionado);
		dispositivoApagado2 = new Dispositivo(null,10,null, TipoDispositivo.AireAcondicionado);

	}

	@Test
	public void tieneDispositivoEncendido_tieneDispositivoInteligenteEncendido_DevuelveTrue() {
		// Arrange
		boolean esperado = true;		
		dispositivoEncendido.setModulo(Modulo.Inteligente);
		dispositivoEncendido.encender(LocalDateTime.now());
		cliente.getDispositivos().add(dispositivoEncendido);
		
		// Act
		boolean obtenido = cliente.tieneDispositivoEncendido();
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void tieneDispositivoEncendido_tieneDispositivoInteligenteApagado_DevuelveFalse() {
		// Arrange
		boolean esperado = false;				
		dispositivoApagado1.setModulo(Modulo.Inteligente);
		cliente.getDispositivos().add(dispositivoApagado1);
		
		// Act
		boolean obtenido = cliente.tieneDispositivoEncendido();
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void tieneDispositivoEncendido_tieneDispositivoEncendidoPeroEsEstandar_DevuelveFalse() {
		// Arrange
		boolean esperado = false;				
		dispositivoApagado1.setModulo(Modulo.Estandar);
		cliente.getDispositivos().add(dispositivoApagado1);
		
		// Act
		boolean obtenido = cliente.tieneDispositivoEncendido();
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void cantidadDeDispositivos_DevuelveTamanioCorrecto() {
		// Arrange
		long esperado = 2;				
		
		cliente.getDispositivos().add(dispositivoEncendido);
		cliente.getDispositivos().add(dispositivoApagado1);
		
		// Act
		long obtenido = cliente.cantidadDispositivos();
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void cantidadDispositivosEncendidos_tieneUnoInteligenteEncendidoYDosEstandarEncendidos_DevuelveUno() {
		// Arrange
		long esperado = 1;		
		dispositivoEncendido.setModulo(Modulo.Inteligente);
		dispositivoEncendido.encender(LocalDateTime.now());
		dispositivoApagado1.setModulo(Modulo.Estandar);
		dispositivoApagado2.setModulo(Modulo.Estandar);
		cliente.getDispositivos().add(dispositivoEncendido);
		cliente.getDispositivos().add(dispositivoApagado1);
		cliente.getDispositivos().add(dispositivoApagado2);
		
		// Act
		long obtenido = cliente.cantidadDispositivosEncendidos();
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void cantidadDispositivosApagados_tieneDosDispositivosInteligentesApagadosYUnoEstandarApagado_Devuelve2() {
		// Arrange
		long esperado = 2;
		dispositivoEncendido.setModulo(Modulo.Estandar);
		dispositivoApagado1.setModulo(Modulo.Inteligente);
		dispositivoApagado2.setModulo(Modulo.Inteligente);
		cliente.getDispositivos().add(dispositivoEncendido);
		cliente.getDispositivos().add(dispositivoApagado1);
		cliente.getDispositivos().add(dispositivoApagado2);
		
		// Act
		long obtenido = cliente.cantidadDispositivosApagados();
		
		// Assert
		assertEquals(esperado, obtenido);
	}

	@Test
	public void consumo_Devuelve160_CuandoSeLePasan3DispositivosCon160kwhEnTotal() {
		// Arrange
		// Hay un modulo estandar que consume 80 por hora, y se usa una hora al día.
		// Hay un módulo inteligente que consume 16 por hora, y se usa 5 horas al día.
		// Un cliente debería poder calcular el consumo total sin importar si son inteligentes o no
		// los dispositivos.
		double esperado = 160;
		LocalDateTime mesPasadoEncendido = LocalDateTime.now()
			.withDayOfMonth(1)
			.minusMonths(1)
			.withHour(10)
			.withMinute(0)
			.withSecond(0)
			.withNano(0);
		dispositivoApagado1.setModulo(Modulo.Inteligente);
		dispositivoEncendido.setModulo(Modulo.Inteligente);
		dispositivoEncendido.setKWHora(16);
		dispositivoEncendido.encender(mesPasadoEncendido);
		dispositivoEncendido.apagar(mesPasadoEncendido.plusHours(5));
		Dispositivo dispositivoEncendido2 = new Dispositivo("", 80, Modulo.Inteligente, TipoDispositivo.AireAcondicionado);
		dispositivoEncendido2.encender(mesPasadoEncendido);
		dispositivoEncendido2.apagar(mesPasadoEncendido.plusHours(1));
		
		cliente.getDispositivos().add(dispositivoEncendido);
		cliente.getDispositivos().add(dispositivoEncendido2);
		cliente.getDispositivos().add(dispositivoApagado1);
		
		// Act
		double obtenido = cliente.consumo();
		
		// Assert
		assertEquals(esperado, obtenido, 0);
	}

	@Test
	public void consumoPorPeriodo_sePasanDosFecha_devuelve300kWh() {
		dispositivoEncendido.setModulo(Modulo.Inteligente);
		// Encendido 1 hora
		dispositivoEncendido.encender(LocalDateTime.parse("1991-10-14T10:00:00"));
		dispositivoEncendido.apagar(LocalDateTime.parse("1991-10-14T11:00:00"));
		// Encendido 2 horas
		dispositivoEncendido.encender(LocalDateTime.parse("1991-10-15T10:00:00"));
		dispositivoEncendido.apagar(LocalDateTime.parse("1991-10-15T12:00:00"));
		cliente.getDispositivos().add(dispositivoEncendido);

		assertEquals(
			300,
			cliente.consumoPorPeriodo(
				LocalDateTime.parse("1991-10-14T00:00:00"),
				LocalDateTime.parse("1991-10-15T23:00:00")
			),
			0.1
		);
	}
}
