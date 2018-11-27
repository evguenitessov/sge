package utils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.Cliente;
import model.Dispositivo;
import model.Zona;

public class ImportadorDeDatos {

	private Lector lectorDeArchivos;

	public ImportadorDeDatos(Lector manejadorArchivos) {
		this.lectorDeArchivos = manejadorArchivos;
	}

	/**
	 * Obtiene un objeto JSON y lo convierte a un Cliente.
	 * @param cliente
	 * @return El cliente parseado.
	 */
	public Cliente deserializarCliente(JsonObject cliente) {
		return new GsonBuilder()
			.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
			.create()
			.fromJson(cliente, Cliente.class);
	}
	
	/**
	 * Deserializa un array de Clientes.
	 * @param clientes
	 * @return La lista de clientes.
	 */
	public List<Cliente> deserializarClientes(JsonArray clientes) {
		List<Cliente> listaClientes = new ArrayList<Cliente>();
		clientes.forEach(cliente -> listaClientes.add(this.deserializarCliente(cliente.getAsJsonObject())));
		return listaClientes;
	}
	
	/**
	 * Obtiene un objeto JSON y lo convierte a un Cliente.
	 * @param dispositivo
	 * @return El dispositivo ya parseado.
	 */
	public Dispositivo deserializarDispositivo(JsonObject dispositivo) {
		return new GsonBuilder()
			.create()
			.fromJson(dispositivo, Dispositivo.class);
	}
    
	/**
	 * Deserializa un array de Dispositivos.
	 * @param dispositivos
	 * @return La lista de dispositivos.
	 */
	public List<Dispositivo> deserializarDispositivos(JsonArray dispositivos) {
		List<Dispositivo> listaDispositivos = new ArrayList<Dispositivo>();
		dispositivos.forEach(dispositivo -> listaDispositivos.add(this.deserializarDispositivo(dispositivo.getAsJsonObject())));
		return listaDispositivos;
	}

	/**
	 * Importa todos los dispositivos de un archivo de dispositivos JSON.
	 * @param filePath El path donde se encuentra el archivo JSON.
	 * @return La lista de dispositivos parseada.
	 * @throws IOException 
	 */
	public List<Dispositivo> importarRepositorioDeDispositivos(String filePath) throws IOException {
		String datosLeidos = this.lectorDeArchivos.leerArchivo(filePath);
		Gson gson = new Gson();					
		
		JsonArray listaDispositivos = gson.fromJson(datosLeidos, JsonElement.class).getAsJsonObject().get("dispositivos").getAsJsonArray();
		List<Dispositivo> dispositivos = this.deserializarDispositivos(listaDispositivos);
		return dispositivos;
		// RepositorioDeDispositivos.getInstance().setDispositivos(gson.fromJson(datosLeidos, RepositorioDeDispositivos.class).getDispositivos());
	}

	/**
	 * Importa todos los Clientes de un archivo de clientes JSON.
	 * @param filePath El path donde se encuentra el archivo JSON.
	 * @return La lista de clientes parseada.
	 * @throws IOException 
	 */
	public List<Cliente> importarRepositorioDeUsuarios(String filePath) throws IOException {
		String datosLeidos = this.lectorDeArchivos.leerArchivo(filePath);
		Gson gson = new Gson();
		JsonArray listaClientes = gson.fromJson(datosLeidos, JsonElement.class).getAsJsonObject().get("clientes").getAsJsonArray();
		List<Cliente> clientes = this.deserializarClientes(listaClientes);
		return clientes;
		// RepositorioDeUsuarios.getInstance().setUsuarios(gson.fromJson(datosLeidos, RepositorioDeUsuarios.class).getUsuarios());
	}

	public Zona deserializarZona(JsonObject zona) {
		return new Gson().fromJson(zona, Zona.class);
	}

	public List<Zona> deserializarZonas(JsonArray zonas) {
		List<Zona> listaZonas = new ArrayList<Zona>();
		zonas.forEach(zona -> listaZonas.add(this.deserializarZona(zona.getAsJsonObject())));
		return listaZonas;
	}

	public List<Zona> importarRepositorioDeZonas(String filePath) throws IOException {
		String datosLeidos = this.lectorDeArchivos.leerArchivo(filePath);
		Gson gson = new Gson();
		JsonArray listaZonas = gson.fromJson(datosLeidos, JsonElement.class).getAsJsonObject().get("zonas").getAsJsonArray();
		return this.deserializarZonas(listaZonas);
	}
}
