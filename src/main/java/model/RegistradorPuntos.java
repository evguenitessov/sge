package model;

import javax.persistence.Entity;

@Entity
public class RegistradorPuntos extends ObservadorDispositivos 
{
	@Override
	public void notificarRegistracionDispositivo(Cliente cliente, Dispositivo dispositivo) 
	{
		if(dispositivo.puedeComunicarseConElSistema()) 
		{
			cliente.sumarPuntos(Puntos.porRegistrarDispositivoInteligente.getPuntos());
		}
	}

	@Override
	public void notificarConversionDispositivo(Cliente cliente) 
	{
		cliente.sumarPuntos(Puntos.porConvertirADispositivoInteligente.getPuntos());
	}
}
