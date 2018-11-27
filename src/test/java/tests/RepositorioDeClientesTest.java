package tests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Cliente;
import repositories.RepositorioDeClientes;

public class RepositorioDeClientesTest {

	List<Cliente> clientes;
	
	Cliente cliente1;
	Cliente cliente2;
	
	@Before
	public void setUp() {	
		clientes = new ArrayList<Cliente>();
		
		cliente1 = new Cliente(null, null, null, null, null, null, null);
		cliente2 = new Cliente(null, null, null, null, null, null, null);
	}
	
	@After
    public void tearDown() {
		RepositorioDeClientes.getInstance().getUsuarios().clear();
	}	

	@Test
	public void persistirEnMemoria() {
		// Arrange				
		clientes.add(cliente1);
		clientes.add(cliente2);
		
		// Act
		RepositorioDeClientes.getInstance().setUsuarios(clientes);
		
		// Assert
		assertEquals(2, RepositorioDeClientes.getInstance().getUsuarios().size());
	}

}
