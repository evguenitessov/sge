package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Before;
import org.junit.Test;

import model.Cliente;
import model.ClienteNoRecomendableException;
import model.Dispositivo;
import model.RecomendadorConsumo;
import repositories.RepositorioDeDispositivos;
import repositories.RepositorioDeClientes;

public class RecomendadorConsumoTest {
    SimplexSolver simplex;
    RecomendadorConsumo recomendador;

    @Before
	public void setUp() throws IOException {
        simplex = new SimplexSolver();
        RepositorioDeDispositivos.getInstance().cargarRepositorio();
        RepositorioDeClientes.getInstance().cargarRepositorio();
        recomendador = new RecomendadorConsumo(612);
	}

    @Test
    public void recomendarHoras_unSoloDispositivo_devuelveHorasMaximas() throws ClienteNoRecomendableException {
        Cliente unCliente = RepositorioDeClientes.getInstance().getUsuarios().get(2);
        Dispositivo aireAcondicionado = unCliente.buscarDispositivoPorNombre("Aire Acondicionado 3500 Frigorias");
        Map<Dispositivo, Double> recomendaciones = recomendador.recomendarHoras(unCliente);

        assertTrue(aireAcondicionado.getUsoMaximo() >= recomendaciones.get(aireAcondicionado));
        assertTrue(aireAcondicionado.getUsoMinimo() <= recomendaciones.get(aireAcondicionado));
        assertEquals(aireAcondicionado.getUsoMaximo(), recomendaciones.get(aireAcondicionado), 0.1);
        assertTrue(recomendador.getRestriccionEnergetica() >= recomendaciones.get(aireAcondicionado) * aireAcondicionado.getKWHora());
    }

    @Test
    public void recomendarHoras_noTieneDispositivos_devuelveDiccionarioVacio() throws ClienteNoRecomendableException {
        Cliente unCliente = RepositorioDeClientes.getInstance().getUsuarios().get(2);
        unCliente.getDispositivos().clear();

        Map<Dispositivo, Double> recomendaciones = recomendador.recomendarHoras(unCliente);

        assertTrue(recomendaciones.isEmpty());
    }

    @Test(expected = ClienteNoRecomendableException.class)
    public void recomendarHoras_noHaySolucion_lanzaClienteNoRecomendableException() throws ClienteNoRecomendableException {
        Cliente unCliente = RepositorioDeClientes.getInstance().getUsuarios().get(2);

        new RecomendadorConsumo(1).recomendarHoras(unCliente);
    }

    @Test
    public void recomendarHoras_tresDispositivos_DosAlMaximoYOtroNo() throws ClienteNoRecomendableException {
        Cliente unCliente = RepositorioDeClientes.getInstance().getUsuarios().get(0);
        Dispositivo aire1 = unCliente.buscarDispositivoPorNombre("Aire Acondicionado 3500 Frigorias");
        Dispositivo aire2 = unCliente.buscarDispositivoPorNombre("Aire Acondicionado 2200 Frigorias");
        Dispositivo lampara = RepositorioDeDispositivos.getInstance().buscarDispositivoPorNombre("Lámpara halógena 15w");
        unCliente.getDispositivos().add(lampara);

        Map<Dispositivo, Double> recomendaciones = recomendador.recomendarHoras(unCliente);

        assertEquals(149.98, recomendaciones.get(aire1), 0.1);
        assertEquals(360, recomendaciones.get(aire2), 0.1);
        assertEquals(360, recomendaciones.get(lampara), 0.1);
    }
}
