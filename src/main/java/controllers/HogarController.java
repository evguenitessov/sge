package controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.FluentIterable;

import model.Cliente;
import model.Usuario;
import repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HogarController {

	
	public static ModelAndView show(Request req, Response res){
		Usuario usuario = req.session().attribute("user");
		Cliente cliente = RepositorioDeUsuarios
			.getInstance()
			.buscarCliente(usuario.getUsername());
		
		Map<String, Object> model = new HashMap<>();
		
		model.put("cliente", cliente);
		model.put("dispositivos", cliente.getDispositivos());
		model.put("hogar", "hogar");
		model.put("consumoUltimoPeriodo", cliente.consumo());
		model.put("mediciones", FluentIterable.from(cliente.mediciones()).limit(10).toList());

		return new ModelAndView(model, "/hogar.hbs");
	}
	
	public static ModelAndView login(Request req, Response res) {
		req.session().attribute("uid", 1);
		res.redirect("/home");
		return null;
	}
	
}
