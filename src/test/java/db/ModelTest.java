package db;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.DoubleFunction;

import org.junit.Ignore;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import model.Actuador;
import model.Cliente;
import model.Dispositivo;
import model.Modulo;
import model.Posicion;
import model.Regla;
import model.Sensor;
import model.TipoDispositivo;
import model.Transformador;

public class ModelTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
	
	@Test
    public void casoDePrueba1() {
        Cliente cliente = new Cliente();
        cliente.setUsername("user-1");
        cliente.setPassword("magia");
        persist(cliente);

        Cliente otroCliente = entityManager().find(Cliente.class, cliente.getUsername());
        otroCliente.setPosicion(new Posicion(1, 1));
        persist(otroCliente);
        
        assertEquals(entityManager().find(Cliente.class, cliente.getUsername()), cliente);
        assertEquals(entityManager().find(Cliente.class, cliente.getUsername()), otroCliente);
        assertEquals(cliente, otroCliente);
    }
	
	@Test
	public void casoDePrueba2() {
        // Un dispositivo que estuvo encendido 3 veces, en total 10 horas, hace 10 días.
        Dispositivo dispositivo = new Dispositivo("Televisor", 1.005, Modulo.Inteligente, TipoDispositivo.Televisor);
        LocalDateTime momentoInicial = LocalDateTime.now().minusDays(10);
        LocalDateTime hace30Dias = momentoInicial.minusDays(20);
        LocalDateTime hoy = hace30Dias.plusDays(30);
        dispositivo.encender(momentoInicial);
        dispositivo.apagar(momentoInicial.plusHours(8));
        dispositivo.encender(momentoInicial.plusDays(1));
        dispositivo.apagar(momentoInicial.plusDays(1).plusHours(1));
        dispositivo.encender(momentoInicial.plusDays(2));
        dispositivo.apagar(momentoInicial.plusDays(2).plusHours(1));
        persist(dispositivo);

        // Checkeamos que esté todo
        Dispositivo dispositivoRecuperado = entityManager()
            .createQuery("from Dispositivo WHERE id = :id", Dispositivo.class)
            .setParameter("id", dispositivo.getId())
            .getSingleResult();
        dispositivoRecuperado.setNombre("Televisor Nuevo");
        persist(dispositivoRecuperado);
        System.out.println("Momentos en que estuvo encendido");
        dispositivoRecuperado.intervalosEntre(hace30Dias, hoy).forEach(
            i -> System.out.println(i.getInicio() + " -> " + i.getFin())
        );
        assertEquals(3, dispositivoRecuperado.intervalosEntre(hace30Dias, hoy).count());
        
        // La última prueba: checkeamos que se modificó efectivamente
        Dispositivo dispositivoOtraVezRecuperado = entityManager()
            .createQuery("from Dispositivo WHERE id = :id", Dispositivo.class)
            .setParameter("id", dispositivo.getId())
            .getSingleResult();
        assertEquals("Televisor Nuevo", dispositivoOtraVezRecuperado.getNombre());
	}
	
    @Ignore("Falta corregir")
    @Test
    public void casoDePrueba3() {	
        Sensor sensor = mock(Sensor.class);
        Actuador actuador = mock(Actuador.class);
        DoubleFunction<Boolean> condicionMayorOMenorDe24Grados = (medicion) -> medicion != 24;
        Regla regla = new Regla(sensor,actuador,condicionMayorOMenorDe24Grados);
        
        persist(regla);
        regla = entityManager().find(Regla.class, regla.getId());
        regla.notificarMedicion(22.00);	
        verify(actuador, times(1)).ejecutar();
        // Revisar...
	}
	
	@Test
    public void casoDePrueba4() {
        List<Transformador> transformadores = entityManager()
            .createQuery("from Transformador", Transformador.class)
            .getResultList();
        int cantidadDeRegistros = transformadores.size();
        
        Transformador transformadorNuevo = new Transformador();
        persist(transformadorNuevo);
        transformadores = entityManager()
            .createQuery("from Transformador", Transformador.class)
            .getResultList();
        assertEquals(cantidadDeRegistros + 1, transformadores.size());
	}
	
    @Test
	public void casoDePrueba5() {
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

        persist(t);
        assertEquals(
            50,
            t.energiaEntre(LocalDateTime.parse("1991-10-14T10:00:00"), LocalDateTime.parse("1991-10-14T13:00:00")),
            0.1
        );

        // Ahora modificamos un dispositivo, y checkeamos que el Transformador se dio cuenta.
        d21.setKWHora(1000);
        persist(d21);

        Transformador tRecuperado = entityManager()
            .createQuery("from Transformador WHERE id = :id", Transformador.class)
            .setParameter("id", t.getId())
            .getSingleResult();
        
        assertEquals(
            3020,
            tRecuperado.energiaEntre(LocalDateTime.parse("1991-10-14T10:00:00"), LocalDateTime.parse("1991-10-14T13:00:00")),
            0.1
        );
    }
}
