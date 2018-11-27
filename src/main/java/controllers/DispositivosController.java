package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import model.Administrador;
import model.DispositivoGenerico;
import model.Modulo;
import model.TipoDispositivo;
import model.Usuario;
import repositories.RepositorioDeDispositivos;
import repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class DispositivosController {

	
	public static ModelAndView show(Request req, Response res){		
		
		Map<String, Object> model = new HashMap<>();		
		
		model.put("modulos", Arrays.asList(Modulo.values()));
		model.put("tipos", Arrays.asList(TipoDispositivo.values()));
		model.put("listado", RepositorioDeDispositivos.getInstance().listarGenericos());
		model.put("dispositivos", "dispositivos");

		Usuario usuario = req.session().attribute("user");
		Administrador admin = RepositorioDeUsuarios
			.getInstance()
			.buscarAdministrador(usuario.getUsername());
				
		model.put("admin", admin);
		
		return new ModelAndView(model, "/dispositivos.hbs");
	}
	
	public static ModelAndView guardar(Request req, Response res) {
		DispositivoGenerico dispositivo = new DispositivoGenerico(
			req.queryParams("name"),
			Double.parseDouble(req.queryParams("kwh")),
			Modulo.values()[Integer.parseInt(req.queryParams("modul"))],
			TipoDispositivo.values()[Integer.parseInt(req.queryParams("type"))]
		);
		
		RepositorioDeDispositivos.getInstance().persistirDispositivo(dispositivo);
		System.out.println(req.queryParams("type"));
		
		res.redirect("/dispositivos");
		
		return null;
	}		
	
}
