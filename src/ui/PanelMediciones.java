package ui;

import handler.Handler;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.JThermometer;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;

import config.Configuracion;

public class PanelMediciones extends JPanel {

	/**
	 * Serial Generada
	 */
	private static final long serialVersionUID = 8445153198275967318L;
	static Logger log = Logger.getLogger(PanelMediciones.class.getName());
	private Handler handler;
	private static PanelMediciones instance = null;
	private JThermometer[] thermo = new JThermometer[5];
	private String[] nombreDeTermometros = { "PT100A", "PT100B", "IRA", "IRB","GABINETE" };
	private String[] nombreDeTacometros = { "ROTOR", "VARIADOR SEW","RESBALAMIENTO" };
	private JFreeChart tacometrosChart[] = new JFreeChart[3];
	private MeterPlot meterplot[] = new MeterPlot[3];
	private DefaultValueDataset data[] = new DefaultValueDataset[3];
	private boolean superaLimiteMedicionAnterior[] = { true, true };
	private Timer timer;

	/**
	 * Constructor Privado
	 * post: crea interfaz de medicion
	 * @param handler
	 */
	private PanelMediciones(Handler handler) {
		this.removeAll();
		setHandler(handler);
		setupTermometros();
		setupTacometros();
		createUI();
	}

	/**
	 * SINGLETON
	 * @param handler
	 * @return nueva instancia o instancia ya invocada previamente
	 */
	public static PanelMediciones getInstance(Handler handler) {
		if (instance == null) {
			instance = new PanelMediciones(handler);
		}
		return instance;
	}

	/**
	 * post: Crea interfaz de usuario, comienza la lectura, y acciona el timer
	 */
	private void createUI() {
		setLayout(new GridLayout(1, 4));
		addComponentes();
		setTimer(new Timer(getHandler().getConfig().getTimerVisualizacion(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<String> mediciones = getHandler().getMedicionesBO().getListaDeMediciones();
				chequearAlarmas(mediciones);
				setMedicionesToComponents(mediciones);
			}
		}));
		getTimer().start();
	}

	/**
	 * pre: se ingresa parametro una lista con las mediciones
	 * post: chequea segun las mediciones ingresadas si sobrepasan limite de temperatura
	 * @param mediciones
	 */
	private void chequearAlarmas(List<String> mediciones) {
		boolean termometrosParaChequear[] = {false, false};
		try{
			for (int termometro = 0; termometro < termometrosParaChequear.length; termometro++) {
				if (temperaturaAlta(Double.parseDouble(mediciones.get(termometro)),
						getHandler().getConfig().getTemperaturaLimite())) {
					if (superaLimiteMedicionAnterior[termometro]) {
						log.warn(this.nombreDeTermometros[termometro]+" sobrepaso la temperatura limite");
						termometrosParaChequear[termometro] = true;
						superaLimiteMedicionAnterior[termometro] = false;
					}
				} else{
					superaLimiteMedicionAnterior[termometro] = true;
				}
			}
			for (int i = 0; i < termometrosParaChequear.length; i++) {
				if(termometrosParaChequear[i]){
					JOptionPane.showMessageDialog(null,
							"SOBREPASO LIMITE DE TEMPERATURA",
							this.nombreDeTermometros[i],
							JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Quilombo1..");
			e.printStackTrace();
			removeAll();
			instance = new PanelMediciones(getHandler());
		}
		catch(NumberFormatException e){
			System.out.println("Quilombo2..");
			e.printStackTrace();
			removeAll();
			instance = new PanelMediciones(getHandler());
		}
	}

	/**
	 * pre: ingresa la temperatura, y el limite
	 * post: si sobrepasa limite, devuelve true
	 * @param temperatura
	 * @param limite
	 * @return
	 */
	private boolean temperaturaAlta(double temperatura, double limite) {
		return temperatura > limite;
	}

	/**
	 * post: agrega todos los componentes al panel (termometros y tacometros)
	 */
	private void addComponentes() {
		add(thermo[0]);
		add(thermo[1]);
		add(thermo[2]);
		add(thermo[3]);
		add(thermo[4]);
		JPanel panelTacometros = new JPanel();
		panelTacometros.setLayout(new GridLayout(3,1));
		
		panelTacometros.add(new ChartPanel(tacometrosChart[0]));
		panelTacometros.add(new ChartPanel(tacometrosChart[1]));
		panelTacometros.add(new ChartPanel(tacometrosChart[2]));
		add(panelTacometros);
	}

	/**
	 * pre: ingresa lista de mediciones
	 * post: setea cada termometro y tacometro con sus respectivas mediciones
	 * @param mediciones
	 */
	private void setMedicionesToComponents(List<String> mediciones) {		
		try{
			getHandler().getConfig();
			if(mediciones.size() == 9 && Configuracion.enabledDatabase()){
				getHandler().insertToDatabase();
			}
			thermo[0].setValue(Double.parseDouble(mediciones.get(0)));
			thermo[1].setValue(Double.parseDouble(mediciones.get(1)));
			thermo[2].setValue(Double.parseDouble(mediciones.get(2)));
			thermo[3].setValue(Double.parseDouble(mediciones.get(3)));
			thermo[4].setValue(Double.parseDouble(mediciones.get(4)));
			data[0].setValue(Double.parseDouble(mediciones.get(5)));
			data[1].setValue(Double.parseDouble(mediciones.get(6)));
			double rotorValue = Double.parseDouble(mediciones.get(5));
			double sewValue = Double.parseDouble(mediciones.get(6));
			double resbalamiento = ((sewValue - rotorValue) / sewValue) * 100;
			data[2].setValue(resbalamiento);
			
		}
		catch(RuntimeException e){
			e.printStackTrace();
			log.debug("OutOfBounds de lista (ctm1) : "+ e.toString());
			removeAll();
			instance = new PanelMediciones(getHandler());
			}
	}

	/**
	 * post: setea configuracion de termometros
	 */
	private void setupTermometros() {
		for (int vector = 0; vector < this.thermo.length; vector++) {
			this.thermo[vector] = new JThermometer();
			this.thermo[vector].setShowValueLines(true);
			this.thermo[vector].setForeground(Color.BLUE);
			this.thermo[vector].addSubtitle(this.nombreDeTermometros[vector],new Font("Verdana", Font.BOLD, 20));
			this.thermo[vector].setValueFormat(new DecimalFormat("#0.0"));
			this.thermo[vector].setValuePaint(Color.BLACK);
			this.thermo[vector].setValueFont(new Font("Verdana", Font.BOLD, 15));
			this.thermo[vector].setFont(new Font("Verdana", Font.BOLD, 15));
			this.thermo[vector].setTickLabelFont(new Font("Verdana", Font.BOLD,20));
			this.thermo[vector].setRange(0, 60);
		}
	}

	/**
	 * post: setea configuracion de tacometros
	 */
	private void setupTacometros() {
		for (int vector = 0; vector < this.meterplot.length; vector++) {
			this.data[vector] = new DefaultValueDataset(0.0);
			this.meterplot[vector] = new MeterPlot(this.data[vector]);
			meterplot[vector].setRange(new Range(0.0D, 65000D));
			meterplot[vector].setDialBackgroundPaint(Color.black);
			meterplot[vector].setNeedlePaint(Color.RED);
			meterplot[vector].setTickPaint(Color.orange);
			meterplot[vector].setTickSize(1000);
			meterplot[vector].setTickLabelPaint(Color.white);
			meterplot[vector].setDrawBorder(true);
			meterplot[vector].setValueFont(new Font("Verdana", Font.BOLD, 18));
			meterplot[vector].setValuePaint(Color.white);
			meterplot[vector].setBackgroundPaint(null);
			meterplot[vector].setUnits("RPM");
			this.tacometrosChart[vector] = new JFreeChart(
					this.nombreDeTacometros[vector], new Font("Verdana",
							Font.BOLD, 18), meterplot[vector], true);
		}
		meterplot[2].setRange(new Range(-10, 10));
		meterplot[2].setTickSize(1);
		meterplot[2].setUnits("%");
	}
	
	/**
	 * pre: ingresa handler principal
	 * post: setea handler
	 * @param handler
	 */
	private void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	/**
	 * post: obtiene handler
	 * @return
	 */
	private Handler getHandler() {
		return this.handler;
	}

	/**
	 * post: devuelve timer
	 * @return
	 */
	public Timer getTimer() {
		return timer;
	}
	
	/**
	 * pre: se ingresa timer
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
}
