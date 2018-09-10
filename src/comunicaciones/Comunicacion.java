package comunicaciones;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.apache.log4j.Logger;
import config.Configuracion;
import exceptions.NoPortException;

public class Comunicacion implements SerialPortEventListener {

	// Atributos
	SerialPort serialPort;
	private List<String> listaDeMediciones;
	
	/**
	 * La lista de mediciones Auxiliar sirve para evitar la desincronizacion del programa.
	 * Cuando el programa detecta un dato del arduino se activa el evento donde se borra la lista,
	 * Si en ese momento, justo despues de que se borro la lista, el Timer la pide, se produce un error
	 * y se tilda el panel de mediciones.
	 */
	private List<String> listaDeMedicionesAuxiliar;
	private String linea;

	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	private Timer timerAdquisicion;
	private Timer timeOutTimer;
	static Logger log = Logger.getLogger(Comunicacion.class.getName());
	private int incrementadorDeInactividad;
	private boolean estadoConexionArduino;
	
	public Comunicacion(){
		setupTimers(); //Timer para verificar el estado de conexion del arduino.
	}
	
	private void setupTimers() {		
		setTimeOutTimer(new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(getIncrementadorDeInactividad()==5){
					log.warn("DESCONEXION USB!");
					close();
					JOptionPane.showMessageDialog(null,
							"1. Cierre el programa. \n"
							+ "2. Verifique la conexion de los sensores. \n"
							+ "3. Desconecte y conecte el cable USB. \n"
							+ "4. Vuelva a iniciar el programa. \n"
							+ "Nota: Si el programa sigue sin conectar reinicie el SEW RTU 1",
							"DESCONEXION USB",
							JOptionPane.ERROR_MESSAGE);
					setIncrementadorDeInactividad(0);
					setEstadoConexionArduino(false);
					try {
						initialize();
					} catch (NoPortException e) {
						e.printStackTrace();
					}
				}
				incrementadorDeInactividad++;
			}
		}));
	}

	public synchronized void initialize() throws NoPortException {
//		if(!getTimerAdquisicion().isRunning())
//			getTimerAdquisicion().start();
		if(!getTimeOutTimer().isRunning())
			getTimeOutTimer().start();
		this.listaDeMediciones = new LinkedList<String>();
		this.listaDeMedicionesAuxiliar = new LinkedList<String>();
		
		getListaDeMediciones().addAll(Arrays.asList(new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0"}));
		getListaDeMedicionesAuxiliar().addAll(Arrays.asList(new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0" }));
		setIncrementadorDeInactividad(0);
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		
		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			
				if (currPortId.getName().equals(Configuracion.getInstance().getPuertoArduino())) {
					portId = currPortId;
				}	
		}
		if (portId == null) {
			String msg = "No se encuentra puerto seteado.";
			log.warn(msg);
			//throw new NoPortException(msg);
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			output = serialPort.getOutputStream();
			
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
	}

	public void write(String msg) throws IOException{
		output.write(msg.getBytes());
		output.flush();
		log.info("Se envia comando: "+msg);
	}
	
	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				setEstadoConexionArduino(true);
				String inputLine = input.readLine();
				getListaDeMedicionesAuxiliar().clear();
				String vectorMedicion[] = inputLine.split("X");
				getListaDeMedicionesAuxiliar().addAll(Arrays.asList(vectorMedicion));
				setListaDeMediciones(getListaDeMedicionesAuxiliar());
				setIncrementadorDeInactividad(0);
				
				linea = "";
				for (String med : vectorMedicion) {
					linea = linea + med + " ";
				}
				linea += Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "-"
						+ Calendar.getInstance().get(Calendar.MINUTE) + "-"
						+ Calendar.getInstance().get(Calendar.SECOND);
				System.out.println(linea);	
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	public List<String> getListaDeMediciones() {
		return listaDeMediciones;
	}

	public void setListaDeMediciones(List<String> listaDeMediciones) {
		this.listaDeMediciones = listaDeMediciones;
	}

	public void addMedicion(String medicion) {
		this.listaDeMediciones.add(medicion);
	}
	
	public List<String> getListaDeMedicionesAuxiliar() {
		return listaDeMedicionesAuxiliar;
	}

	public void setListaDeMedicionesAuxiliar(List<String> listaDeMedicionesAuxiliar) {
		this.listaDeMedicionesAuxiliar = listaDeMedicionesAuxiliar;
	}

	public Timer getTimerAdquisicion() {
		return timerAdquisicion;
	}

	public void setTimerAdquisicion(Timer timerAdquisicion) {
		this.timerAdquisicion = timerAdquisicion;
	}

	public Timer getTimeOutTimer() {
		return timeOutTimer;
	}

	public void setTimeOutTimer(Timer timeOutTimer) {
		this.timeOutTimer = timeOutTimer;
	}

	public int getIncrementadorDeInactividad() {
		return incrementadorDeInactividad;
	}

	public void setIncrementadorDeInactividad(int incrementadorDeInactividad) {
		this.incrementadorDeInactividad = incrementadorDeInactividad;
	}

	public boolean isEstadoConexionArduino() {
		return estadoConexionArduino;
	}

	public void setEstadoConexionArduino(boolean estadoConexionArduino) {
		this.estadoConexionArduino = estadoConexionArduino;
	}
}
