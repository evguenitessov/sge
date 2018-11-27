package tests;

import java.util.function.DoubleFunction;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import model.Actuador;
import model.Sensor;
import model.Regla;

public class ReglaTest {

	Actuador actuador;
	Sensor sensor;
	Regla regla;
	DoubleFunction<Boolean> condicionMayorOMenorDe24Grados;
	
	@Before
	public void setUp() {
		sensor = mock(Sensor.class);
		actuador = mock(Actuador.class);
		condicionMayorOMenorDe24Grados = (medicion) -> medicion != 24;
		regla = new Regla(sensor,actuador,condicionMayorOMenorDe24Grados);
	}

	@Test
	public void dispararRegla_SeCumpleLaCondicion_SeEjecutaElActuador() 
	{
		// Act
		regla.notificarMedicion(22.00);
		
		// Assert
		verify(actuador, times(1)).ejecutar();
	}
	
	@Test
	public void dispararRegla_NoSeCumpleLaCondicion_NoHaceNada() 
	{
		// Act
		regla.notificarMedicion(24.00);
		
		// Assert
		verify(actuador, never()).ejecutar();
	}
}
