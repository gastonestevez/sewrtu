package bo;

import java.io.IOException;
import java.util.List;

import comunicaciones.Comunicacion;
import exceptions.NoPortException;

public class MedicionesBO {
	// Atributos
	private Comunicacion comunicacion;

	// Constructor
	public MedicionesBO() {
		setComunicacion(new Comunicacion());
	}

	// Metodos	
	public boolean getEstadoConexionArduino(){
		return getComunicacion().isEstadoConexionArduino();
	}
	
	// Comenzar medicion
	public void comenzarMedicion() throws NoPortException {
		getComunicacion().initialize();
		getComunicacion().getTimeOutTimer().start();
	}
	
	// Terminar medicion
	public void terminarMedicion(){
		getComunicacion().close();
		if(getComunicacion().getTimeOutTimer() != null)
			getComunicacion().getTimeOutTimer().stop();
	}

	// getMediciones
	public List<String> getListaDeMediciones() {
		return getComunicacion().getListaDeMediciones();
	}
	
	public void enviarMensaje(String msg) throws IOException{
		getComunicacion().write(msg);
	}

	public void setComunicacion(Comunicacion comunicacion) {
		this.comunicacion = comunicacion;
	}

	public Comunicacion getComunicacion() {
		return this.comunicacion;
	}

}
