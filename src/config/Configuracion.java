package config;

public class Configuracion {

	//Atributos
	private String PuertoArduino = "COM9";
	private int TemperaturaLimite = 100;
	//private int IntervaloAdquisicion = 3000;
	private int TimerGrafico = 15000;
	private int TimerVisualizacion = 2000;
	private static Configuracion config; 
	private int velocidadSew;
	private static boolean enabledDatabase = false;
	
	//Constructor
	private Configuracion(){
		setVelocidadSew(25);
	}
	
	//Metodos
	
	public static boolean enabledDatabase(){
		return enabledDatabase;
	}
	
	public void setEnabledDatabase(boolean var){
		this.enabledDatabase = var;
	}
	public String getPuertoArduino() {
		return PuertoArduino;
	}

	public void setPuertoArduino(String puertoArduino) {
		PuertoArduino = puertoArduino;
	}

	public int getTemperaturaLimite() {
		return TemperaturaLimite;
	}

	public void setTemperaturaLimite(int temperaturaLimite) {
		TemperaturaLimite = temperaturaLimite;
	}

	public int getTimerGrafico() {
		return TimerGrafico;
	}

	public void setTimerGrafico(int timerGrafico) {
		TimerGrafico = timerGrafico;
	}

	public int getTimerVisualizacion() {
		return TimerVisualizacion;
	}

	public void setTimerVisualizacion(int timerVisualizacion) {
		TimerVisualizacion = timerVisualizacion;
	}

	public static Configuracion getInstance(){
		if(config == null){
			config = new Configuracion();
		}
		return config;
	}

	@Override
	public String toString() {
		return "Configuracion [PuertoArduino=" + PuertoArduino + ", TemperaturaLimite=" + TemperaturaLimite
			    + ", TimerGrafico=" + TimerGrafico + ", TimerVisualizacion=" + TimerVisualizacion + "]";
	}
	
	public int getVelocidadSew() {
		return velocidadSew;
	}
	
	public void setVelocidadSew(int velocidadSew) {
		if(velocidadSew > 19 && velocidadSew < 256)
			this.velocidadSew = velocidadSew;
	}
}
