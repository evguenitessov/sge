package repositories;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

import model.Dispositivo;
import model.DispositivoGenerico;
import utils.ImportadorDeDatos;
import utils.Lector;

public class RepositorioDeDispositivos {
	private static RepositorioDeDispositivos instance = new RepositorioDeDispositivos();
	private List<Dispositivo> dispositivos;
	
	private RepositorioDeDispositivos() {
		
	}
	
	public static RepositorioDeDispositivos getInstance() {
		return instance;
	}

	public void cargarRepositorio() throws IOException {
		this.dispositivos = new ImportadorDeDatos(new Lector())
			.importarRepositorioDeDispositivos("./src/test/resources/Dispositivos.json");
	}

	public Dispositivo buscarDispositivoPorNombre(String nombreDispositivo) {
		return this.getDispositivos()
			.stream()
			.filter(d -> d.getNombre().equals(nombreDispositivo))
			.findFirst()
			.get();
	}

	public List<Dispositivo> getDispositivos() {
		return dispositivos;
	}

	public void setDispositivos(List<Dispositivo> dispositivos) {
		this.dispositivos = dispositivos;
	}
	
	public List<DispositivoGenerico> listarGenericos() {
		List<DispositivoGenerico> genericos = PerThreadEntityManagers.getEntityManager()
            .createQuery("from DispositivoGenerico", DispositivoGenerico.class)
			.getResultList();

		return genericos;
	}
	
	public void persistirDispositivo(DispositivoGenerico dispositivo) {
		EntityManager entityManager = PerThreadEntityManagers.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(dispositivo);
		entityManager.getTransaction().commit();
	}		
}
