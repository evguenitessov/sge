package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import model.Administrador;

public class AdministradorTest {

	@Test
	public void mesesSiendoAdministradorHasta_Devuelve3_CuandoPasaron3MesesJustos() {
		// Arrange
		Administrador administrador = new Administrador("Max", "Planck", "Berlín 123", "662606957", LocalDate.parse("2018-01-01"));
		long esperado = 3;
		
		// Act
		long obtenido = administrador.mesesSiendoAdministradorHasta(LocalDate.parse("2018-04-01"));
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void mesesSiendoAdministradorHasta_Devuelve0_CuandoPasoMenosDeUnMes() {
		// Arrange
		Administrador administrador = new Administrador("Max", "Planck", "Berlín 123", "662606957", LocalDate.parse("2018-01-01"));
		long esperado = 0;
		
		// Act
		long obtenido = administrador.mesesSiendoAdministradorHasta(LocalDate.parse("2018-01-20"));
		
		// Assert
		assertEquals(esperado, obtenido);
	}
	
	@Test
	public void mesesSiendoAdministradorHasta_Devuelve2_CuandoPasoMasDe2MesesPeroMenosDe3Meses() {
		// Arrange
		Administrador administrador = new Administrador("Max", "Planck", "Berlín 123", "662606957", LocalDate.parse("2018-01-01"));
		long esperado = 2;
		
		// Act
		long obtenido = administrador.mesesSiendoAdministradorHasta(LocalDate.parse("2018-03-28"));
		
		// Assert
		assertEquals(esperado, obtenido);
	}

}
