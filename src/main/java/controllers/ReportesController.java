package controllers;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.Administrador;
import model.Transformador;
import model.Usuario;
import repositories.RepositorioDeTransformadores;
import repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ReportesController {

	
	public static ModelAndView show(Request req, Response res){
		Map<String, Object> model = new HashMap<>();
		
		Usuario usuario = req.session().attribute("user");
		Administrador admin = RepositorioDeUsuarios
			.getInstance()
			.buscarAdministrador(usuario.getUsername());
						
		model.put("admin", admin);
		model.put("reportes", "reportes");				
		
		return new ModelAndView(model, "/reportes.hbs");
	}
	
	public static ModelAndView listado(Request req, Response res){
		Map<String, Object> model = new HashMap<>();
		
		Usuario usuario = req.session().attribute("user");
		Administrador admin = RepositorioDeUsuarios
			.getInstance()
			.buscarAdministrador(usuario.getUsername());
		
		Collection<Transformador> transformadores = RepositorioDeTransformadores.getInstance().buscarTodos();
		Map<Long, Double> consumosTransformadores = new HashMap<>();
		
		String desde = req.queryParams("fechaD");
		String hasta = req.queryParams("fechaH");		
		
		for (Transformador transformador : transformadores) {
			if(desde == null || hasta == null) {
				consumosTransformadores.put(transformador.getId(), 0.0);
			}
			else {
				Double consumo = transformador.energiaEntre(
					LocalDateTime.parse(desde + "T00:00:00"), 
					LocalDateTime.parse(hasta + "T00:00:00")
				);
				//consumo = Math.floor(consumo * 100) / 100;
				consumosTransformadores.put(transformador.getId(), consumo);
			}			
		}					
						
		model.put("consumosClientes", consumosTransformadores);				
		model.put("admin", admin);
		model.put("reportes", "reportes");				
		
		return new ModelAndView(model, "/reportes.hbs");
	}
	
}
