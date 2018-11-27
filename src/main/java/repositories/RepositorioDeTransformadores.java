package repositories;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

import model.Transformador;

public class RepositorioDeTransformadores {

	private static RepositorioDeTransformadores instance = null;
	
	private RepositorioDeTransformadores() {}
	
	public static RepositorioDeTransformadores getInstance() {
		if(instance == null) {
			instance = new RepositorioDeTransformadores();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Collection<Transformador> buscarTodos() {
		EntityManager em = PerThreadEntityManagers.getEntityManager();
		Query q = em.createQuery("SELECT e FROM Transformador AS e");
		return (Collection<Transformador>) q.getResultList();
	}
	
}
