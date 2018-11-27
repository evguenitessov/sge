public class Application {

//	public static void main(String[] args) {				
//		Spark.port(9000);
//		Spark.get("/", (request, response) -> "{ \"bienvenida:\" 1 }" + request.queryParams("nombre")); // query params
////		Spark.get("/:nombre", (request, response) -> "{ \"mensaje:\" 1 }" + request.params("nombre")); // path params
////		Spark.get("/usuario", ControllerUsuario::get); 
//		
//		//ResponseTransformer transformer = new HandlebarsTemplateEngine();
//		// el get del controller va a tener que devolver un ModelAndView
//		// new ModelAndView(usuario, "perfil.hbs");
//		// los archivos estatios van en src/main/resources/templates donde va el .hbs
//		// para mostrar una propiedad del modelo, por ej {{nombre}} que se traduce a get nombre 
//		// {{ captura.[0].apodo }}
//		
//		Spark.init();
//		
//		
//
////		RecomendadorConsumo recomendador = new RecomendadorConsumo(612);
////
////		List<Dispositivo> dispositivos = new ArrayList<Dispositivo>();
////		Cliente cliente = new Cliente(null, null, null, null, null, null, null, null, dispositivos, new Posicion(0, 0));
////		
////		
////		Dispositivo dispositivoEncendido = new Dispositivo(null,1.613,2,null, TipoDispositivo.AireAcondicionado);
////		dispositivoEncendido.setModulo(ModuloFuncionamiento.INTELIGENTE);		
////		
////		cliente.getDispositivos().add(dispositivoEncendido);		
////		
////		Map<Dispositivo, Double> recomendaciones = null;
////		try {
////			recomendaciones = recomendador.recomendarHoras(cliente);
////			System.out.println("Recomendacion maxima de horas para dispositivoEncendido: " + recomendaciones.get(dispositivoEncendido));			
////		} catch (ClienteNoRecomendableException e) { 
////			e.printStackTrace();
////		}        
////
////		
////		System.out.println("Consumo diario del cliente: " + cliente.consumo());
////		System.out.println("Consumo del dipositivo en septiembre: " + 
////				dispositivoEncendido.consumoEntre(LocalDateTime.parse("2018-09-01T00:00:00"), LocalDateTime.parse("2018-09-20T00:00:00")));
////		System.out.println("Horas que estuvo en uso el dispositivo: " + 
////				dispositivoEncendido.consumoEntre(LocalDateTime.parse("2018-09-01T00:00:00"), 
////						LocalDateTime.parse("2018-09-20T00:00:00")) / dispositivoEncendido.getkWHora());
////		
////		double recomendacion = recomendaciones.get(dispositivoEncendido);
////		
////		Timer timer = new Timer (2000, new ActionListener () 
////		{ 
////			@Override
////		    public void actionPerformed(ActionEvent e) 
////		    { 
////		        System.out.println(LocalDateTime.now());
////		        for(Dispositivo dispositivo : cliente.getDispositivos()) {
////		        	if ((dispositivo.consumoEntre(LocalDateTime.parse("2018-09-01T00:00:00"), 
////							LocalDateTime.parse("2018-09-20T00:00:00")) / dispositivo.getkWHora()) > recomendacion) {
////		        		System.out.println("Apagando dispositivo...");
////					}
////		        }
////		    }					
////		}); 		
////
////		timer.start();
////		
////		try {
////			Thread.sleep(60000);
////		} catch (InterruptedException e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
//			
//		
//		
//	
//	}

}
