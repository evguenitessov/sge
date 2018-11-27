package repositories;

import java.io.IOException;
import java.util.List;

import model.Cliente;
import utils.*;

public class RepositorioDeClientes {
	private static RepositorioDeClientes instance = new RepositorioDeClientes();
	private List<Cliente> clientes;
	
	private RepositorioDeClientes() {
		
	}
	
	public static RepositorioDeClientes getInstance() {
		return instance;
	}

	public void cargarRepositorio() throws IOException {
		this.clientes = new ImportadorDeDatos(new Lector())
			.importarRepositorioDeUsuarios("./src/test/resources/Clientes.json");
	}

	public List<Cliente> getUsuarios() {
		return clientes;
	}

	public void setUsuarios(List<Cliente> usuarios) {
		this.clientes = usuarios;
	}
}