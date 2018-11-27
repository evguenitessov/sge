package utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Lector {
	/**
	 * Lee todo el contenido de un archivo de texto.
	 * @param filePath El path donde se encuentra el archivo.
	 * @return El contenido del archivo.
	 * @throws IOException 
	 */
	public String leerArchivo(String filePath) throws IOException {
		try {
			 File file = new File(filePath);
			 return FileUtils.readFileToString(file);
		} catch (IOException e) {
			throw new IOException("Error leyendo el archivo: " + filePath);
		}
	}
}
