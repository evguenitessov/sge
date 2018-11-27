package controllers;

import java.util.HashMap;
import java.util.Map;

import model.Cliente;
import model.ClienteNoRecomendableException;
import model.RecomendadorConsumo;
import model.Usuario;
import repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class SimplexController {
	
	public static ModelAndView show(Request req, Response res) throws ClienteNoRecomendableException {
		Usuario usuario = req.session().attribute("user");
		Cliente cliente = RepositorioDeUsuarios
			.getInstance()
			.buscarCliente(usuario.getUsername());
		
		Map<String, String> recomendaciones = new HashMap<>();
		RecomendadorConsumo recomendador = new RecomendadorConsumo(612);
		recomendador.recomendarHoras(cliente).forEach(
			(d, r) -> recomendaciones.put(d.getNombre(), String.format("%.2f", r))
		);
		
		Map<String, Object> model = new HashMap<>();
		
		model.put("recomendaciones", recomendaciones);				
		model.put("cliente", cliente);
		model.put("simplex", "simplex");
		
		return new ModelAndView(model, "simplex.hbs");
	}
}
