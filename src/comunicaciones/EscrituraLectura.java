package comunicaciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class EscrituraLectura {
	
	static Logger log = Logger.getLogger(EscrituraLectura.class.getName());
	
	public static void escribir(String linea, String directorio) {

		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(directorio);
			pw = new PrintWriter(fichero);
			pw.println(linea);

		} catch (Exception e) {
			log.error("Falla en escritura de archivo..." + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static List<String> leer(String directorio) {
		List<String> lineas = new LinkedList<String>();
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			archivo = new File(directorio);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			String linea = "";

			while ((linea = br.readLine()) != null)
				lineas.add(linea);

		} catch (Exception e) {
			log.error("Error en lectura de archivo... "+e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lineas;
	}

	public static String leerUltimaLinea(String directorio) {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		String linea = "";

		try {
			archivo = new File(directorio);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			String readLine = "";
			while ((readLine = br.readLine()) != null)
				linea = readLine;
		} catch (Exception e) {
			log.error("Error en leer ultima linea de archivo... " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return linea;
	}
}