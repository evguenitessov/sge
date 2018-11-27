package controllers;

import java.util.HashMap;
import java.util.Map;

import model.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HomeController {
	public static ModelAndView home(Request req, Response res){
		Map<String, Object> model = new HashMap<>();
		
		Usuario usuario = req.session().attribute("user");
		
		if (usuario.esAdmin()) {
			model.put("admin", req.session().attribute("user"));
		}
		else {
			model.put("cliente", req.session().attribute("user"));
		}
				
		
		return new ModelAndView(model, "home/home.hbs");
	}
	
}
