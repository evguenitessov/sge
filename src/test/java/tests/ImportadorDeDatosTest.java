package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Categoria;
import model.Cliente;
import model.Dispositivo;
import model.Modulo;
import model.TipoDispositivo;
import utils.ImportadorDeDatos;
import utils.Lector;

public class ImportadorDeDatosTest {
	
	Lector manejador;
	ImportadorDeDatos importador;
	Lector manejadorPosta;
	ImportadorDeDatos importadorPosta;
	
	@Before
	public void setUp() {	
		
		manejador = mock(Lector.class);
		importador = new ImportadorDeDatos(manejador);
		manejadorPosta = new Lector();
		importadorPosta = new ImportadorDeDatos(manejadorPosta);
	}	

	@Test
	public void ImportadorDeDatos_deserializaCliente() throws IOException {
		// Arrange

		String contenidoJSON = "{\"clientes\": ["
			+ "{\"nombre\": \"Juan\","
			+ "\"apellido\": \"Quinteros\","
			+ "\"tipoDocumento\": \"DNI\","
			+ "\"numeroDocumento\": \"36159783\","
			+ "\"telefono\": \"41111111\","
			+ "\"domicilio\": \"Calle Falsa 123\","
			+ "\"fechaAlta\": \"1991-10-14\","
			+ "\"categoria\": \"R1\""
			+ "}]}";

		// Act
		when(manejador.leerArchivo(anyString())).thenReturn(contenidoJSON);
		List<Cliente> clientes = importador.importarRepositorioDeUsuarios("C:/alguna_ruta_que_sea_posta");

		// Assert
		assertEquals(1, clientes.size());
		assertEquals("Juan", clientes.get(0).getNombre());
		assertEquals("Quinteros", clientes.get(0).getApellido());
		assertEquals("DNI", clientes.get(0).getTipoDocumento());
		assertEquals("36159783", clientes.get(0).getNumeroDocumento());
		assertEquals("41111111", clientes.get(0).getTelefono());
		assertEquals("Calle Falsa 123", clientes.get(0).getDomicilio());
		assertTrue(clientes.get(0).getFechaAlta().equals(LocalDate.of(1991, 10, 14)));
		assertEquals(Categoria.R1, clientes.get(0).getCategoria());
	}

	@Test
	public void ImportadorDeDatos_deserializaDispositivo() throws IOException {
		// Arrange		
		String contenidoJSON = "{\"dispositivos\": ["
			+ "{\"nombre\": \"Pava electrica\","
			+ "\"kWHora\": 5"
			+ "}]}";

		// Act
		when(manejador.leerArchivo(anyString())).thenReturn(contenidoJSON);
		List<Dispositivo> dispositivos = importador.importarRepositorioDeDispositivos("C:/alguna_ruta_que_sea_posta");

		// Assert
		dispositivos.get(0).setModulo(Modulo.Inteligente);
		assertEquals(1, dispositivos.size());
		assertEquals("Pava electrica", dispositivos.get(0).getNombre());
		assertEquals((double) 5, dispositivos.get(0).getKWHora(), 0.01);
	}
	
	@Test
	public void ImportadorDeDatos_deserializaClienteDeArchivoJSON() throws IOException {
		// Act
		List<Cliente> clientes = importadorPosta.importarRepositorioDeUsuarios("./src/test/resources/Clientes.json");

		// Assert
		assertEquals(3, clientes.size());
		assertEquals("Juan", clientes.get(0).getNombre());
		assertEquals("Quinteros", clientes.get(0).getApellido());
		assertEquals("DNI", clientes.get(0).getTipoDocumento());
		assertEquals("36159783", clientes.get(0).getNumeroDocumento());
		assertEquals("41111111", clientes.get(0).getTelefono());
		assertEquals("Calle Falsa 123", clientes.get(0).getDomicilio());
		assertTrue(clientes.get(0).getFechaAlta().equals(LocalDate.of(1991, 10, 14)));
		assertEquals(Categoria.R1, clientes.get(0).getCategoria());
	}	
	
	@Test
	public void ImportadorDeDatos_deserializaDispositivoDeArchivoJSON() throws IOException {
		// Act		
		List<Dispositivo> dispositivos = importadorPosta.importarRepositorioDeDispositivos("./src/test/resources/Dispositivos.json");
		Dispositivo dispositivo = dispositivos.get(0);

		// Assert
		assertEquals(25, dispositivos.size());
		assertEquals(Modulo.Estandar, dispositivo.getModulo());
		assertEquals("Pava electrica", dispositivo.getNombre());
		assertEquals((double) 5, dispositivo.getKWHora(), 0.01);
		assertEquals(TipoDispositivo.Microondas, dispositivo.getTipoDispositivo());
	}
	
	@Test(expected = IOException.class)
	public void ImportadorDeDatos_relanzaExcepcionDeIO_cuandoElLectorLanzaExcepcionDeIO() throws IOException {
		// Act
		when(manejador.leerArchivo(anyString())).thenThrow(IOException.class);
		importador.importarRepositorioDeDispositivos("ruta/que/no/existe/excepcion.txt");
	}

}
