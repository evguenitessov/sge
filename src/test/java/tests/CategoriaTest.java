package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.Before;

import model.Categoria;
import model.Cliente;
import model.Dispositivo;
import model.Modulo;
import model.TipoDispositivo;

public class CategoriaTest {

	Cliente cliente;
	Dispositivo dispositivo1;
	Dispositivo dispositivo2;
	Dispositivo dispositivo3;
	Dispositivo dispositivo4;
	
	@Before
	public void setUp() {	
		cliente = new Cliente(null, null, null, null, null, null, null);
		dispositivo1 = new Dispositivo(null,50, Modulo.Inteligente, TipoDispositivo.AireAcondicionado);
		dispositivo2 = new Dispositivo(null,100, Modulo.Inteligente, TipoDispositivo.AireAcondicionado);
		dispositivo3 = new Dispositivo(null,200, Modulo.Inteligente, TipoDispositivo.AireAcondicionado);
		dispositivo4 = new Dispositivo(null,1200, Modulo.Inteligente, TipoDispositivo.AireAcondicionado);
	}

	@Test
	public void categorizarCliente_devuelveR3_cuandoSeLeAsignanDispositivosCon350DeConsumoTotal() 
	{
		// Arrange
		cliente.getDispositivos().add(dispositivo1);
		cliente.getDispositivos().add(dispositivo2);
		cliente.getDispositivos().add(dispositivo3);
		LocalDateTime mesPasadoEncendido = LocalDateTime.now()
			.withDayOfMonth(1)
			.minusMonths(1)
			.withHour(10);
		LocalDateTime mesPasadoApagado = mesPasadoEncendido.plusHours(1);
		dispositivo1.encender(mesPasadoEncendido);
		dispositivo1.apagar(mesPasadoApagado);
		dispositivo2.encender(mesPasadoEncendido);
		dispositivo2.apagar(mesPasadoApagado);
		dispositivo3.encender(mesPasadoEncendido);
		dispositivo3.apagar(mesPasadoApagado);
		Categoria categoriaEsperada = Categoria.R3;


		// Act
		Categoria categoriaObtenida = Categoria.categorizar(cliente);
		
		// Assert
		assertEquals(categoriaEsperada, categoriaObtenida);
	}
	
	@Test
	public void categorizarCliente_devuelveR1_cuandoSeLeAsignanDispositivosCon50DeConsumoTotal() 
	{
		// Arrange
		cliente.getDispositivos().add(dispositivo1);
		LocalDateTime momentoEncendido = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).withHour(10);
		dispositivo1.encender(momentoEncendido);
		dispositivo1.apagar(momentoEncendido.plusHours(1));
		Categoria categoriaEsperada = Categoria.R1;

		// Act
		Categoria categoriaObtenida = Categoria.categorizar(cliente);

				
		// Assert
		assertEquals(categoriaEsperada, categoriaObtenida);
	}
	
	@Test
	public void categorizarCliente_devuelveR1_cuandoSeLeAsignanDispositivosConMasDe1400DeConsumoTotal()
	{
		// Arrange
		LocalDateTime momentoEncendido = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).withHour(10);
		cliente.getDispositivos().add(dispositivo1);
		dispositivo1.encender(momentoEncendido);
		dispositivo1.apagar(momentoEncendido.plusHours(1));
		cliente.getDispositivos().add(dispositivo2);
		dispositivo2.encender(momentoEncendido);
		dispositivo2.apagar(momentoEncendido.plusHours(1));
		cliente.getDispositivos().add(dispositivo3);
		dispositivo3.encender(momentoEncendido);
		dispositivo3.apagar(momentoEncendido.plusHours(1));
		cliente.getDispositivos().add(dispositivo4);
		dispositivo4.encender(momentoEncendido);
		dispositivo4.apagar(momentoEncendido.plusHours(1));
		Categoria categoriaEsperada = Categoria.R9;

		// Act
		Categoria categoriaObtenida = Categoria.categorizar(cliente);

			
		// Assert
		assertEquals(categoriaEsperada, categoriaObtenida);
	}
	
	@Test
	public void recategorizarCliente_recategorizaClienteAR9_cuandoCorresponde()
	{
		// Arrange
		Cliente cliente = new Cliente(null, null, null, null, null, null, null);
		LocalDateTime momentoEncendido = LocalDateTime.now().withDayOfMonth(1).minusMonths(1).withHour(10);
		cliente.getDispositivos().add(dispositivo1);
		dispositivo1.encender(momentoEncendido);
		dispositivo1.apagar(momentoEncendido.plusHours(1));
		cliente.getDispositivos().add(dispositivo2);
		dispositivo2.encender(momentoEncendido);
		dispositivo2.apagar(momentoEncendido.plusHours(1));
		cliente.getDispositivos().add(dispositivo3);
		dispositivo3.encender(momentoEncendido);
		dispositivo3.apagar(momentoEncendido.plusHours(1));
		cliente.getDispositivos().add(dispositivo4);
		dispositivo4.encender(momentoEncendido);
		dispositivo4.apagar(momentoEncendido.plusHours(1));
		Categoria categoriaEsperada = Categoria.R9;

		// Act
		Categoria.recategorizar(cliente);

			
		// Assert
		assertEquals(categoriaEsperada, cliente.getCategoria());
	}
}
