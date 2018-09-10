package ui;

import handler.Handler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.apache.log4j.Logger;

public class FramePrincipal extends JFrame {
	private static final long serialVersionUID = 3756696714064124464L;

	//Atributos
	private Handler handler;
	private Timer chequeoDeLuces;
	private JLabel puntoEstadoVacio;
	private JLabel puntoEstadoParadaDeEmergencia;
	private JLabel estado;
	static Logger log = Logger.getLogger(FramePrincipal.class.getName());
	private int flag = 0; // La logica del flag se utiliza para evitar falsos positivos de la parada de emergencia al pausar programa.
	
	//Constructor
	public FramePrincipal(String title,Handler handler){
		super(title);
		this.handler = handler;
		log.info("Cargando Frame Principal...");
		setChequeoDeLuces(new Timer(700, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					String estadoVacio = getHandler().getMedicionesBO().getListaDeMediciones().get(7);
					String estadoEstop = getHandler().getMedicionesBO().getListaDeMediciones().get(8);			
					if(estadoVacio.equals("1")){
						getPuntoEstadoVacio().setForeground(Color.GREEN);
					}else{
						getPuntoEstadoVacio().setForeground(Color.GRAY);
					}
					if(getHandler().consultarConexionArduino()){	
						if(estadoEstop.equals("0")){
							if(flag<5)
								flag=flag+1;
							else
								flag=3;
							System.out.println(flag);
							if(flag==2){
								getPuntoEstadoParadaDeEmergencia().setForeground(Color.RED);
								log.warn("PARADA DE EMERGENCIA ACTIVADA");
								JOptionPane.showMessageDialog(null,"Se presionó la Parada de Emergencia","Parada de Emergencia",JOptionPane.WARNING_MESSAGE);
								flag=3;
							}
						}else{
							getPuntoEstadoParadaDeEmergencia().setForeground(Color.GRAY);
							flag=0;
						}
					}
				}catch(RuntimeException e){
					e.printStackTrace();
				}
			}
		}));
		createUI();
	}
	
	private void createUI(){
		setJMenuBar(createMenuBar());
		getContentPane().setLayout(new BorderLayout());
		setSize(1366, 760);
		JLabel background = new JLabel(new ImageIcon("res/logocnea.gif"));
		background.setLayout(new FlowLayout());
		add(background);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Mata el proceso cuando apreto la X
	}

	private JMenuBar createMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		JMenuItem menuConfig = new JMenuItem("Configuracion");
		Font fuente = new Font("Arial", Font.BOLD, 18);
		
		final JMenuItem menuMediciones = new JMenuItem("Mediciones");
		final JMenuItem menuMotor = new JMenuItem("Motor");
		final JMenuItem menuGrafico = new JMenuItem("Grafico");
		final JMenuItem menuDistancia = new JMenuItem("Modulo Distancia");
		
		menuGrafico.setEnabled(false);
		JMenuItem menuAyuda = new JMenuItem("Acerca de..");
		
		JMenu mArchivo = new JMenu("Archivo");
		mArchivo.setFont(fuente);
		menuConfig.setFont(fuente);
		mArchivo.add(menuConfig);
		menuBar.add(mArchivo);
		menuDistancia.setFont(fuente);
		
		JMenu mVisualizacion = new JMenu("Visualización");
		mVisualizacion.setFont(fuente);
		menuMediciones.setFont(fuente);
		menuMotor.setFont(fuente);
		menuGrafico.setFont(fuente);
		mVisualizacion.add(menuMediciones);
		mVisualizacion.add(menuMotor);
		mVisualizacion.add(menuGrafico);
		mVisualizacion.add(menuDistancia);
		menuBar.add(mVisualizacion);
		
		JMenu mAyuda = new JMenu("Ayuda");
		menuAyuda.setFont(fuente);
		mAyuda.setFont(fuente);
		mAyuda.add(menuAyuda);
		menuBar.add(mAyuda);
		
		final JButton play = new JButton("Play");
		play.setFont(fuente);
		menuBar.add(play);
		
		final JButton pause = new JButton("Pause");
		pause.setFont(fuente);
		menuBar.add(pause);
		pause.setEnabled(false);
		menuMediciones.setEnabled(false);
		menuMotor.setEnabled(false);
			
		setPuntoEstadoVacio(new JLabel("  • "));
		getPuntoEstadoVacio().setFont(new Font("Arial", Font.BOLD, 32));
		final JLabel estadoVacio = new JLabel(" Vacio ");
		estadoVacio.setFont(fuente);
		setPuntoEstadoParadaDeEmergencia(new JLabel("  • "));
		getPuntoEstadoParadaDeEmergencia().setFont((new Font("Arial",Font.BOLD,32)));
		final JLabel estadoParadaDeEmergencia = new JLabel(" Parada de Emergencia");
		estadoParadaDeEmergencia.setFont(fuente);
		JLabel estado = new JLabel("                         Stopped");
		estado.setFont(fuente);
		
		menuBar.add(getPuntoEstadoVacio());
		menuBar.add(estadoVacio);
		menuBar.add(getPuntoEstadoParadaDeEmergencia());
		menuBar.add(estadoParadaDeEmergencia);
		menuBar.add(estado);

		//Listener
		menuMediciones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				handler.consultaMediciones();
			}
		});
		
		//Listener
		menuMotor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handler.consultaMotor();
			}
		});
		
		//Listener
		menuGrafico.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handler.consultaGraficos();
			}
		});
		
		menuDistancia.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.consultaDistancia();
			}
		});
		
		this.addWindowListener(new java.awt.event.WindowAdapter(){
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent){
				System.out.println("Chau");
				handler.getMedicionesBO().terminarMedicion();
				System.exit(0);
			}
		});
			
		//Listener
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(handler.comenzarMediciones()){
					pause.setEnabled(true);
					play.setEnabled(false);
					menuMediciones.setEnabled(true);
					menuMotor.setEnabled(false);
					menuGrafico.setEnabled(true);
					estado.setText("                         Running");
					estado.setForeground(Color.BLUE);
					
					log.info("Iniciando mediciones...");
					
					log.info("Cargando Timer de panel: Graficos");
					PanelGraficos.getInstance(getHandler()).getTimer().start();
					
					log.info("Cargando Mediciones...");
					PanelMediciones.getInstance(getHandler());
					if(!PanelMediciones.getInstance(null).getTimer().isRunning())
						PanelMediciones.getInstance(null).getTimer().start();
					
					getChequeoDeLuces().start();			
				}else{
					JOptionPane.showMessageDialog(null,"Puerto no encontrado","Menu Principal",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		//Listener
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				play.setEnabled(true);
				pause.setEnabled(false);
				estado.setText("                         Stopped");
				estado.setForeground(Color.RED);

				log.info("Deteniendo mediciones...");
				getHandler().terminarMediciones();
				log.info("Deteniendo Timer del panel: Graficos...");
				PanelGraficos.getInstance(getHandler()).getTimer().stop();
				log.info("Deteniendo Timer del panel: Mediciones...");
				PanelMediciones.getInstance(null).getTimer().stop();
				getChequeoDeLuces().stop();
			}
		});
				
		menuAyuda.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handler.consultaAyuda();
			}
		});
		
		menuConfig.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handler.consultaConfig();
			}
		});	
		return menuBar;
	}
	
	public void cambiarPanel(JComponent component) {
		getContentPane().removeAll();
		getContentPane().add(BorderLayout.CENTER, component);
		getContentPane().validate();
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}


	public Timer getChequeoDeLuces() {
		return chequeoDeLuces;
	}

	public void setChequeoDeLuces(Timer chequeoDeLuces) {
		this.chequeoDeLuces = chequeoDeLuces;
	}
	
	public JLabel getPuntoEstadoVacio() {
		return puntoEstadoVacio;
	}
	
	public void setPuntoEstadoVacio(JLabel puntoEstadoVacio) {
		this.puntoEstadoVacio = puntoEstadoVacio;
	}
	
	public JLabel getPuntoEstadoParadaDeEmergencia() {
		return puntoEstadoParadaDeEmergencia;
	}
	
	public void setPuntoEstadoParadaDeEmergencia(JLabel puntoEstadoParadaDeEmergencia) {
		this.puntoEstadoParadaDeEmergencia = puntoEstadoParadaDeEmergencia;
	}
	
	public JLabel getEstado() {
		return estado;
	}
	
	public void setEstado(JLabel estado) {
		this.estado = estado;
	}
}
