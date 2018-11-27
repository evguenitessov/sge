package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Cliente;
import model.ClienteNoPerteneceALaZonaException;
import model.Dispositivo;
import model.Modulo;
import model.Posicion;
import model.TipoDispositivo;
import model.Transformador;
import model.Zona;
import utils.ImportadorDeDatos;
import utils.Lector;

public class ZonaTest {
	
    Zona parqueChacabuco;
    Zona obelisco;
    Cliente cliente1;
    Cliente cliente2;
    Cliente cliente3;
    List<Zona> zonas;
    List<Transformador> transformadores;
    List<Cliente> clientes;
	
	@Before
	public void setUp() throws IOException {
        ImportadorDeDatos importador = new ImportadorDeDatos(new Lector());	
        this.zonas = importador.importarRepositorioDeZonas("./src/test/resources/Zonas.json");
        this.clientes = importador.importarRepositorioDeUsuarios("./src/test/resources/Clientes.json");
        // Conectamos a todos los Clientes a sus respectivos Transformadores
        this.zonas.forEach(zona -> zona.conectarClientesDeLaZona(clientes));
        parqueChacabuco = zonas.stream().filter(z -> z.getDescripcion().equalsIgnoreCase("Parque Chacabuco")).findFirst().get();
        obelisco = zonas.stream().filter(z -> z.getDescripcion().equalsIgnoreCase("Obelisco")).findFirst().get();
        cliente2 = clientes.stream().filter(c -> c.getNombre().equalsIgnoreCase("Nikola")).findFirst().get();
        cliente1 = clientes.stream().filter(c -> c.getNombre().equalsIgnoreCase("Juan")).findFirst().get();
        cliente3 = clientes.stream().filter(c -> c.getNombre().equalsIgnoreCase("Evgueni")).findFirst().get();
    }
    
    @Test
    public void conectarAlTransformadorMasCercano_hayUnSoloTransformador_seConectaAlUnico() {
        assertTrue(parqueChacabuco.estaConectadoATransformador(cliente1));
    }

    @Test
    public void conectarAlTransformadorMasCercano_hayDosTransformadores_seConectaAlMasCercano() {
        // En Obelisco hay dos Transformadores, vamos a ver si eligió el más cercano...
        Transformador transformadorDelCliente = obelisco.transformadorDe(cliente2);
        Transformador otroTransformador = obelisco
            .getTransformadores()
            .stream()
            .filter(t -> !t.equals(transformadorDelCliente))
            .findAny()
            .get();

        assertTrue(obelisco.estaConectadoATransformador(cliente2));
        assertTrue(cliente2.distanciaHasta(transformadorDelCliente) < cliente2.distanciaHasta(otroTransformador));
    }

    @Test(expected = ClienteNoPerteneceALaZonaException.class)
    public void conectarAlTransformadorMasCercano_elClienteNoPerteneceALaZona_lanzaClienteNoPerteneceALaZonaException() {
        // Este Cliente está en Parque Chacabuco, no está en Obelisco
        obelisco.conectarAlTransformadorMasCercano(cliente1);
    }

    @Test
    public void energiaSuministrada_tieneUnSoloCliente_devuelveElConsumoDelCliente() {
        assertEquals(cliente1.consumoActual() + cliente3.consumoActual(), cliente1.getTransformador().energiaSuministrada(), 0.1);
        assertEquals(cliente2.consumoActual(), cliente2.getTransformador().energiaSuministrada(), 0.1);
    }

    @Test
    public void consumoTotal_hayDosClientes_devuelveLaSumatoriaDeSusConsumos() {
        cliente1.conectarseA(cliente2.getTransformador());

        assertEquals(
            cliente1.consumoActual() + cliente2.consumoActual(),
            cliente1.getTransformador().energiaSuministrada(), 0.1
        );
        assertEquals(
            cliente1.consumoActual() + cliente2.consumoActual(),
            obelisco.consumoTotal(), 0.1
        );
    }

    @Test
    public void contieneA_unClienteQueEstaEnSuZona_devuelveTrue() {
        assertTrue(parqueChacabuco.contieneA(cliente1));
    }

    @Test
    public void contieneA_unClienteQueEstaApenitasEnLaZona_devuelveTrue() {
        cliente2.setPosicion(new Posicion(-34.633229, -58.430541));
        assertTrue(parqueChacabuco.contieneA(cliente2));
    }

    @Test
    public void contieneA_unClienteQueNoEstaEnLaZona_devuelveFalse() {
        cliente2.setPosicion(new Posicion(-34.633093, -58.429819));
        assertFalse(parqueChacabuco.contieneA(cliente2));
    }

    @Test
    public void energiaEntre_dosClientes_devuelve50kWh() {
        Transformador t = new Transformador();
        // Primer cliente con dos dispositivos encendidos una hora.
        Cliente c1 = new Cliente("A", "B", "DNI", "36159783", "12345678", "Falsa 123", null);
        Dispositivo d11 = new Dispositivo("D", 10, Modulo.Inteligente, TipoDispositivo.Computadora);
        d11.encender(LocalDateTime.parse("1991-10-14T10:00:00"));
        d11.apagar(LocalDateTime.parse("1991-10-14T11:00:00"));
        Dispositivo d12 = new Dispositivo("D", 10, Modulo.Inteligente, TipoDispositivo.Computadora);
        d12.encender(LocalDateTime.parse("1991-10-14T10:30:00"));
        d12.apagar(LocalDateTime.parse("1991-10-14T11:30:00"));
        c1.getDispositivos().add(d11);
        c1.getDispositivos().add(d12);
        // Segundo cliente tiene un dispositivo encendido 3 horas.
        Cliente c2 = new Cliente("A", "B", "DNI", "36159783", "12345678", "Falsa 123", null);
        Dispositivo d21 = new Dispositivo("D", 10, Modulo.Inteligente, TipoDispositivo.Computadora);
        d21.encender(LocalDateTime.parse("1991-10-14T10:00:00"));
        d21.apagar(LocalDateTime.parse("1991-10-14T13:00:00"));
        c2.getDispositivos().add(d21);
        // Clientes en el transformador
        t.getClientes().add(c1);
        t.getClientes().add(c2);

        assertEquals(
            50,
            t.energiaEntre(LocalDateTime.parse("1991-10-14T10:00:00"), LocalDateTime.parse("1991-10-14T13:00:00")),
            0.1
        );
    }
}
