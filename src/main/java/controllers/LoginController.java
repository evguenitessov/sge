package controllers;

import java.util.HashMap;
import java.util.Map;

import model.Usuario;
import repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class LoginController {
	
	public static ModelAndView getLogInPage(Request req, Response res) {
		Map<String, Boolean> model = new HashMap<>();
		model.put("loggedOut", true);
		return new ModelAndView(model, "home/login.hbs");
	}
	
	public static ModelAndView login(Request req, Response res) { 		
		Map<String, Object> model = new HashMap<>();		
		
		Usuario usuario = RepositorioDeUsuarios.getInstance().buscarUsuario(req.queryParams("username"));
				
		String passwordHash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(req.queryParams("password"));
							
		if(usuario != null && usuario.passwordMatches(passwordHash)) {
			req.session().attribute("user", usuario);
			res.redirect("/home");
			return null;
		}
		
		model.put("loginFailed", true);
		return new ModelAndView(model, "home/login.hbs");
	}	
	
	public static void verificarUsuarioLoggeado(Request req, Response res) {
        if(!req.pathInfo().equals("/login")) {
            res.redirect("/login");
        }
    }
	
}
