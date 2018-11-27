package repositories;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

import model.Administrador;
import model.Cliente;
import model.Usuario;

public class RepositorioDeUsuarios {
	
	private static RepositorioDeUsuarios instance = null;
	
	private RepositorioDeUsuarios() {}
	
	public static RepositorioDeUsuarios getInstance() {
		if(instance == null) {
			instance = new RepositorioDeUsuarios();
		}
		return instance;
	}
	
	public Usuario buscarUsuario(String username) {
		return PerThreadEntityManagers.getEntityManager().find(Usuario.class, username);
	}

	public Cliente buscarCliente(String username) {
		return PerThreadEntityManagers.getEntityManager().find(Cliente.class, username);
	}
	
	public Administrador buscarAdministrador(String username) {
		return PerThreadEntityManagers.getEntityManager().find(Administrador.class, username);
	}

	@SuppressWarnings("unchecked")
	public Collection<Usuario> buscarTodos() {
		EntityManager em = PerThreadEntityManagers.getEntityManager();
		Query q = em.createQuery("SELECT e FROM Usuario AS e");
		return (Collection<Usuario>) q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Cliente> buscarTodosClientes() {
		EntityManager em = PerThreadEntityManagers.getEntityManager();
		Query q = em.createQuery("SELECT e FROM Usuario AS e where dtype = 1");		
		return (Collection<Cliente>) q.getResultList();
	}
}