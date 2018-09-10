package ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import handler.Handler;

public class PanelGraficos extends JPanel {

	private static final long serialVersionUID = 7262206594631501684L;

	// Atributos
	static Logger log = Logger.getLogger(PanelGraficos.class.getName());
	private static PanelGraficos instance;
	private XYSeries temperaturaInterior;
	private XYSeries temperaturaExterior;
	private XYSeries temperaturaBaseCilindro;
	private XYSeries temperaturaTopCilindro;
	private XYSeries temperaturaAmbiente;
	private XYSeries tacometroVelocidadCilindro;
	private XYSeries tacometroVelocidadVariador;
	private XYSeriesCollection coleccionesDeSeries[];
	private JFreeChart charts[];
	private ChartPanel chartpanels[];
	private double minuto;
	private Timer timer;
	private Handler handler;
	private int incrementador;
	private int acumuladorMinutos;
	private int medicionAnterior;
	private int cantidadMuestras = 2000; 	// Cantidad de muestras que puede graficar antes de reiniciarse el gráfico.
											// 1 muestra cada 15 seg --> 4 muestras por minuto , 240 muestras por hora , 5760 muestras por día  (peor caso)
											// 1 muestra cada 60 seg --> 1 muestra por minuto , 60 muestras por hora , 1440 muestras por día    (mejor caso)
											// Las muestras se toman cada 15 seg siempre que se detecte una aceleración, en caso contrario las muestras se hacen cada 1 min.
	
	private PanelGraficos(Handler handler) {
		setMedicionAnterior(0);
		setHandler(handler);
		setIncrementador(0);
		log.info("Creando Panel Graficos...");
		this.minuto = 0.0;
		setAcumuladorMinutos(1);
		setupPaneles();
	}

	private void setupPaneles() {
		inicializacionDeVariables();
		//seteoDeSeries(getHandler().getMedicionesBO().getListaDeMediciones());
		seteoDeColecciones();
		armarGraficos();
		setupTimer();
		createUI();		
	}

	private void createUI() {
		setLayout(new GridLayout(3, 1));
		agregarGraficosAlPanel();
	}

	private void setupTimer() {
		setTimer(new Timer(getHandler().getConfig().getTimerGrafico(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					minuto += (getHandler().getConfig().getTimerGrafico()+0.0) / 1000.0 / 3600.0  ;
					if(incrementador==cantidadMuestras){
						minuto = 0;
						incrementador = 0;
						reiniciarPanel();
					}else{
					int medicionActual = Integer.parseInt(getHandler().getMedicionesBO().getListaDeMediciones().get(6));
					if(medicionActual != getMedicionAnterior() && 
							(medicionActual<getMedicionAnterior()-10 || medicionActual>getMedicionAnterior()+10)){
						addValores(getHandler().getMedicionesBO().getListaDeMediciones());
						log.fatal(getHandler().getMedicionesBO().getListaDeMediciones().toString());						
						setAcumuladorMinutos(1);
					}else{
						if(getAcumuladorMinutos()==4){
							addValores(getHandler().getMedicionesBO().getListaDeMediciones());
							log.fatal(getHandler().getMedicionesBO().getListaDeMediciones().toString());
							setAcumuladorMinutos(1);
						}else{
							setAcumuladorMinutos(getAcumuladorMinutos()+1);
						}
					}
					setMedicionAnterior(medicionActual);
					armarGraficos();
					setIncrementador(getIncrementador()+1);
					repaint();
					revalidate();
					}
				}catch(RuntimeException e){
					e.printStackTrace();
					repaint();
					revalidate();
				}
			}
		}));
		getTimer().start();
	}

	public static PanelGraficos getInstance(Handler handler){
		if(instance==null){
			instance = new PanelGraficos(handler);
		}
		return instance;
	}

	private void reiniciarPanel(){
		temperaturaInterior = null;
		temperaturaExterior = null;
		temperaturaAmbiente = null;
		temperaturaBaseCilindro= null;
		temperaturaTopCilindro= null;
		tacometroVelocidadCilindro= null;
		tacometroVelocidadVariador= null;
		coleccionesDeSeries = null;
		for (int i = 0; i < 2; i++) {
			chartpanels[i] = null;
		}
		getTimer().stop();
		setTimer(null);
		removeAll();
		System.gc();
		setupPaneles();
		repaint();
		revalidate();
	} 
	
	private void agregarGraficosAlPanel() {
		add(chartpanels[0]);
		add(chartpanels[1]);
		add(chartpanels[2]);		
	}

	private void armarGraficos() {
		charts[0] = ChartFactory.createXYLineChart("Temperaturas",
				"Tiempo [Horas]", "Temperatura [°C]",
				coleccionesDeSeries[0]);
		chartpanels[0] = new ChartPanel(charts[0]);
		
		charts[1] = ChartFactory.createXYLineChart("Temperaturas",
				"Tiempo [Horas]", "Temperatura [°C]",
				coleccionesDeSeries[1]);
		chartpanels[1] = new ChartPanel(charts[1]);
		
		charts[2] = ChartFactory.createXYLineChart("Velocidad",
				"Tiempo [Horas]", "Revoluciones",
				coleccionesDeSeries[2]);
		chartpanels[2] = new ChartPanel(charts[2]);
		
	}

	private void inicializacionDeVariables() {
		temperaturaInterior = new XYSeries("Temperatura Motor Interior");
		temperaturaExterior = new XYSeries("Temperatura Motor Exterior");
		temperaturaAmbiente = new XYSeries("Temperatura Ambiente");
		temperaturaBaseCilindro = new XYSeries("Temperatura Base Cilindro");
		temperaturaTopCilindro = new XYSeries("Temperatura Top Cilindro");
		tacometroVelocidadCilindro = new XYSeries("Tacometro Cilindro");
		tacometroVelocidadVariador = new XYSeries("Tacometro Sew");
		coleccionesDeSeries = new XYSeriesCollection[3];
		charts = new JFreeChart[3];
		chartpanels = new ChartPanel[3];
		
	}

	private void seteoDeColecciones() {
		for (int i = 0; i < coleccionesDeSeries.length; i++) {
			coleccionesDeSeries[i] = new XYSeriesCollection();
			switch (i) {
			case 0:
				coleccionesDeSeries[i].addSeries(temperaturaInterior);
				coleccionesDeSeries[i].addSeries(temperaturaExterior);
				coleccionesDeSeries[i].addSeries(temperaturaAmbiente);
				break;
			case 1:
				coleccionesDeSeries[i].addSeries(temperaturaBaseCilindro);
				coleccionesDeSeries[i].addSeries(temperaturaTopCilindro);
				coleccionesDeSeries[i].addSeries(temperaturaAmbiente);
				break;
			case 2:
				coleccionesDeSeries[i].addSeries(tacometroVelocidadCilindro);
				coleccionesDeSeries[i].addSeries(tacometroVelocidadVariador);
				break;
			}
		}
	}
	
	private void addValores(List<String> listaDeMediciones){
		//String vectorLinea[] = linea.split(" ");
		double valorDeTemperaturaInterior = Double
				.parseDouble(listaDeMediciones.get(0));
		double valorDeTemperaturaExterior = Double
				.parseDouble(listaDeMediciones.get(1));
		double valorDeTemperaturaAmbiente = Double
				.parseDouble(listaDeMediciones.get(4));
		double valorDeTemperaturaBaseCilindro = Double
				.parseDouble(listaDeMediciones.get(2));
		double valorDeTemperaturaTopCilindro = Double
				.parseDouble(listaDeMediciones.get(3));
		double valorDeTacometroVelocidadCilindro = Double
				.parseDouble(listaDeMediciones.get(5));
		double valorDeTacometroVelocidadVariador = Double
				.parseDouble(listaDeMediciones.get(6));

		temperaturaInterior.add(minuto, valorDeTemperaturaInterior);
		temperaturaExterior.add(minuto, valorDeTemperaturaExterior);
		temperaturaAmbiente.add(minuto, valorDeTemperaturaAmbiente);

		temperaturaBaseCilindro.add(minuto, valorDeTemperaturaBaseCilindro);
		temperaturaTopCilindro.add(minuto, valorDeTemperaturaTopCilindro);
		temperaturaAmbiente.add(minuto, valorDeTemperaturaAmbiente);

		tacometroVelocidadCilindro.add(minuto,
				valorDeTacometroVelocidadCilindro);
		tacometroVelocidadVariador.add(minuto,
				valorDeTacometroVelocidadVariador);
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public int getIncrementador() {
		return incrementador;
	}

	public void setIncrementador(int incrementador) {
		this.incrementador = incrementador;
	}

	public int getMedicionAnterior() {
		return medicionAnterior;
	}

	public void setMedicionAnterior(int i) {
		this.medicionAnterior = i;
	}

	public int getAcumuladorMinutos() {
		return acumuladorMinutos;
	}

	public void setAcumuladorMinutos(int acumuladorMinutos) {
		this.acumuladorMinutos = acumuladorMinutos;
	}
}
