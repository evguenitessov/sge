package server;

import controllers.ConsumoPorPeriodoController;
import controllers.ConsumosHogaresController;
import controllers.DispositivosController;
import controllers.HogarController;
import controllers.HomeController;
import controllers.LoginController;
import controllers.ReportesController;
import controllers.SimplexController;
import model.Usuario;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.utils.BooleanHelper;
import spark.utils.HandlebarsTemplateEngineBuilder;
import spark.utils.LocalDateTimeHelper;

import static spark.Spark.halt;

public class Router {

	public static void configure() {			
		HandlebarsTemplateEngine engine = HandlebarsTemplateEngineBuilder
				.create()
				.withDefaultHelpers()
				.withHelper("isTrue", BooleanHelper.isTrue)
				.withHelper("dateTime", LocalDateTimeHelper.dateTime)
				.build();

		Spark.staticFiles.location("/public");
		
		Spark.before("/", LoginController::verificarUsuarioLoggeado);		
		
		Spark.get("/login", LoginController::getLogInPage, engine);
		Spark.post("/login", LoginController::login, engine);		
		Spark.get("/home", HomeController::home, engine);
		Spark.get("/reportes", ReportesController::show, engine);
		Spark.get("/dispositivos", DispositivosController::show, engine);
		Spark.post("/reportes", ReportesController::listado, engine);
		Spark.post("/dispositivos", DispositivosController::guardar, engine);
		Spark.get("/consumoPorPeriodo", ConsumoPorPeriodoController::show, engine);
		Spark.get("/simplex", SimplexController::show, engine);
		Spark.get("/hogar", HogarController::show, engine);
		Spark.get("/consumosHogares", ConsumosHogaresController::show, engine);
		
		Spark.before("/dispositivos", (req, res) -> {
			verificarAdministrator(req, res);
		});
			
		Spark.before("/reportes", (req, res) -> {
			verificarAdministrator(req, res);
		});
		
		Spark.before("/consumosHogares", (req, res) -> {
			verificarAdministrator(req, res);
		});
		
		Spark.before("/simplex", (req, res) -> {
			verificarCliente(req, res);
		});

		Spark.before("/consumoPorPeriodo", (req, res) -> {
			verificarCliente(req, res);
		});

		Spark.before("/hogar", (req, res) -> {
			verificarCliente(req, res);
		});
	}

	private static void verificarCliente(Request req, Response res) {
		Usuario usuario = (Usuario) req.session().attribute("user");
		if (usuario.esAdmin()) {
			res.redirect("/login");
			halt(401, "No tiene permisos para acceder a los recursos de cliente");
		} 
	}

	private static void verificarAdministrator(Request req, Response res) {
		Usuario usuario = (Usuario) req.session().attribute("user");
		if (!usuario.esAdmin()) {
			res.redirect("/login");
			halt(401, "No tiene permisos para acceder a los recursos de administrador");
		}
	}

}
