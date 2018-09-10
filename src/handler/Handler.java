package handler;

import java.awt.BorderLayout;
import java.awt.List;
import java.io.IOException;

import bo.MedicionesBO;
import comunicaciones.ConectorMysql;
import config.Configuracion;
import exceptions.NoPortException;
import ui.FramePrincipal;
import ui.PanelAyuda;
import ui.PanelConfig;
import ui.PanelDistancia;
import ui.PanelGraficos;
import ui.PanelMediciones;
import ui.PanelMotor;

public class Handler {
	//Atributos
	private FramePrincipal frame;
	private MedicionesBO medicionesBO;
	
	//Constructor
	public Handler(){
		this.frame = new FramePrincipal("Mediciones 1.8",this);
		setMedicionesBO(new MedicionesBO());
	}
	
	//Metodos
	
	//Inicio: Hace visible el frame (principal). El frame es el que contiene los paneles.
	public void init() {
		frame.setVisible(true);
	}
	
	public void setMedicionesBO(MedicionesBO medicionesBO){
		this.medicionesBO = medicionesBO;
	}
	
	public MedicionesBO getMedicionesBO(){
		return this.medicionesBO;
	}
	
	public boolean comenzarMediciones(){
		try {
			getMedicionesBO().comenzarMedicion();
			return true;
		} catch (NoPortException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void terminarMediciones(){
		getMedicionesBO().terminarMedicion();
	}
	
	//CONSULTAS de paneles
	//Todas las consultas devuelven una instancia. Esto permite traer el objeto ya creado en vez de crear otro.
	
	public void consultaMediciones() {
		frame.cambiarPanel(PanelMediciones.getInstance((this)));
		frame.repaint();
	}
	
	public void consultaGraficos(){
		frame.cambiarPanel(PanelGraficos.getInstance(this));
		frame.repaint();
	}
	
	public void consultaAyuda(){
		frame.cambiarPanel(new PanelAyuda(this));
	}
	
	public void consultaConfig(){
		frame.cambiarPanel(PanelConfig.getInstance(this));
		frame.repaint();
	}
	
	//Devuelve el estado del Arduino
	public boolean consultarConexionArduino(){
		return getMedicionesBO().getEstadoConexionArduino();
	}
	
	public Configuracion getConfig(){
		return Configuracion.getInstance();
	}
	
	public void consultaMotor(){
//		frame.cambiarPanel(PanelMotor.getInstance(this));
//		frame.repaint();
		PanelMotor.getInstance(this).setLayout(new BorderLayout());
		//PanelMotor.getInstance(this).setSize(330,230);
		PanelMotor.getInstance(this).setVisible(true);
	}
	
	//Envia mensaje de on/off al motor
	public boolean enviarMensaje(String msg){
		try {
			getMedicionesBO().enviarMensaje(msg);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Seteo de velocidad para el motor
	public void fijarVelocidad(int velocidad){
		getConfig().setVelocidadSew(velocidad);
	}
	
	public int getVelocidadActual(){
		return getConfig().getVelocidadSew();
	}

	public void insertToDatabase() {
		java.util.List<String> lista = getMedicionesBO().getListaDeMediciones();
		ConectorMysql conector = new ConectorMysql(
				Double.parseDouble(lista.get(0)), //ira
				Double.parseDouble(lista.get(1)), //irb
				Double.parseDouble(lista.get(2)), //ambiente
				Double.parseDouble(lista.get(5)), //cilindro
				Double.parseDouble(lista.get(6)), //sew
				Integer.parseInt(lista.get(7)), //vacio
				Integer.parseInt(lista.get(8)) //emergencia
				);
		conector.insertData();
	}

	public void consultaDistancia() {
		frame.cambiarPanel(PanelDistancia.getInstance(this));
		frame.repaint();
	}

}
