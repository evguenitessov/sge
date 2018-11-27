package controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.Administrador;
import model.Cliente;
import model.Usuario;
import repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ConsumosHogaresController {
	
	public static ModelAndView show(Request req, Response res){
		Map<String, Object> model = new HashMap<>();
		
		Usuario usuario = req.session().attribute("user");
		Administrador admin = RepositorioDeUsuarios
			.getInstance()
			.buscarAdministrador(usuario.getUsername());
		
		Collection<Cliente> clientes = RepositorioDeUsuarios.getInstance().buscarTodosClientes();
		Map<String, Double> consumosClientes = new HashMap<>();
		
		clientes.forEach((c) -> consumosClientes.put(c.getNombre(), c.consumoActual()));
				
		model.put("consumosClientes", consumosClientes);
		model.put("admin", admin);
		model.put("consumosHogares", "consumosHogares");				
		
		return new ModelAndView(model, "/consumosHogares.hbs");
	}

}
