package tests;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import model.Cliente;
import model.Dispositivo;
import model.RegistradorPuntos;

public class RegistradorPuntosTest {
	
	RegistradorPuntos registradorPuntos;
	Cliente cliente;
	Dispositivo dispositivo;
	
	@Before
	public void setUp() {
		registradorPuntos = new RegistradorPuntos();
		cliente = mock(Cliente.class);
		dispositivo = mock(Dispositivo.class);
	}

	@Test
	public void notificarRegistracionDispositivo_elDispositivoNoEsInteligente_NoHaceNada() 
	{
		// Arrange
		when(dispositivo.puedeComunicarseConElSistema()).thenReturn(false);
		
		// Act
		registradorPuntos.notificarRegistracionDispositivo(cliente, dispositivo);
		
		// Assert
		verify(cliente, never()).sumarPuntos(anyInt());
	}
	
	@Test
	public void notificarRegistracionDispositivo_elDispositivoEsInteligente_SumaPuntos() 
	{
		// Arrange
		when(dispositivo.puedeComunicarseConElSistema()).thenReturn(true);
		
		// Act
		registradorPuntos.notificarRegistracionDispositivo(cliente, dispositivo);
		
		// Assert
		verify(cliente, times(1)).sumarPuntos(anyInt());
	}
	
	@Test
	public void notificarConversionDispositivo_SumaPuntos() 
	{
		// Arrange
		
		// Act
		registradorPuntos.notificarConversionDispositivo(cliente);
		
		// Assert
		verify(cliente, times(1)).sumarPuntos(anyInt());
	}

}
