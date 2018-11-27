package controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import model.Cliente;
import model.Usuario;
import repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ConsumoPorPeriodoController {

	
	public static ModelAndView show(Request req, Response res){
		Usuario usuario = req.session().attribute("user");
		Cliente cliente = RepositorioDeUsuarios
			.getInstance()
			.buscarCliente(usuario.getUsername());
		
		Map<String, Object> model = new HashMap<>();
		
		model.put("cliente", cliente);
		model.put("consumoPorPeriodo", "consumoPorPeriodo");

		// Tenemos que verificar si el consumo que vamos a mostrar es el actual, o es resultado de una consulta
		String desde = req.queryParams("fechaDesde");
		String hasta = req.queryParams("fechaHasta");
		
		if(desde == null || hasta == null) {
			model.put("consumo", cliente.consumoActual());
		}
		else {
			Double consumo = cliente.consumoPorPeriodo(
				LocalDateTime.parse(desde + "T00:00:00"), 
				LocalDateTime.parse(hasta + "T00:00:00")
			);
			model.put("consumo", consumo);
		}
		model.put("fechaDesde", desde);
		model.put("fechaHasta", hasta);

		return new ModelAndView(model, "/consumoPorPeriodo.hbs");
	}
	
	public static ModelAndView login(Request req, Response res) {
		req.session().attribute("uid", 1);
		res.redirect("/home");
		return null;
	}
	
}
