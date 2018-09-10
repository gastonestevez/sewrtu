package ui;

import handler.Handler;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelMotor extends JFrame {

	private static final long serialVersionUID = 565052750737691868L;
	//Atributos
	private Handler handler;
	private JButton botonEncendido;
	private JButton subirVelocidad;
	private JButton bajarVelocidad;
	private JLabel velocidadActual;
	private static PanelMotor panel;
	
	//Constructor
	private PanelMotor(Handler handler){
		super("Motor");
		setHandler(handler);
		createUI();
	}
	
	private void createUI(){
		setBotonEncendido(new JButton("Encendido / Apagado"));
		setSubirVelocidad(new JButton("Subir velocidad"));
		setBajarVelocidad(new JButton("Bajar velocidad"));
		setVelocidadActual(new JLabel(""+getHandler().getVelocidadActual()));
		getVelocidadActual().setHorizontalAlignment(JLabel.CENTER);
		getVelocidadActual().setFont(new Font("Sanserif",Font.BOLD,18));
		JLabel tituloEncendido = new JLabel("Panel Encendido");
		tituloEncendido.setHorizontalAlignment(JLabel.CENTER);
		JLabel tituloVelocidad = new JLabel("Panel Velocidad");
		tituloVelocidad.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel panelVelocidad = new JPanel();
		JPanel panelEncendido = new JPanel();
		
		panelEncendido.setLayout(new GridLayout(1,1));
		panelEncendido.add(getBotonEncendido());
		panelEncendido.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		panelVelocidad.setLayout(new GridLayout(3,1));
		panelVelocidad.add(getSubirVelocidad());
		panelVelocidad.add(getVelocidadActual());
		panelVelocidad.add(getBajarVelocidad());
		panelVelocidad.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		
		JPanel everything = new JPanel();
		everything.setLayout(new GridLayout(2,2));
		everything.add(tituloEncendido);
		everything.add(tituloVelocidad);
		everything.add(panelEncendido);
		everything.add(panelVelocidad);
		getContentPane().removeAll();
		getContentPane().add(BorderLayout.CENTER,everything);
		getContentPane().validate();
		getContentPane().repaint();
		//getContentPane().pack();
		setLocation(500,500);
		pack();
		setearEventos();
	}
	
	private void setearEventos() {
		getBotonEncendido().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getHandler().enviarMensaje("sew");
			}
		});
		
		getSubirVelocidad().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getHandler().fijarVelocidad(getHandler().getVelocidadActual()+5);
				getHandler().enviarMensaje("velocidad "+getHandler().getVelocidadActual());
				getVelocidadActual().setText(""+getHandler().getVelocidadActual());
				if(!chequearLimiteDeVelocidad()){
					getBajarVelocidad().setEnabled(true);
				}
			
			}

		});
		
		getBajarVelocidad().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getHandler().fijarVelocidad(getHandler().getVelocidadActual()-5);
				getVelocidadActual().setText(""+getHandler().getVelocidadActual());

				if(chequearLimiteDeVelocidad()){
					getHandler().enviarMensaje("sew");
					getBajarVelocidad().setEnabled(false);
				}else{
					getHandler().enviarMensaje("velocidad "+getVelocidadActual());
				}
			}
		});
	}
	
	private boolean chequearLimiteDeVelocidad() {
		return getHandler().getVelocidadActual()==20;
	}
	
	public static PanelMotor getInstance(Handler handler){
		if(panel==null){
			panel = new PanelMotor(handler);
		}
		return panel;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public JButton getBotonEncendido() {
		return botonEncendido;
	}

	public void setBotonEncendido(JButton botonEncendido) {
		this.botonEncendido = botonEncendido;
	}

	public JButton getSubirVelocidad() {
		return subirVelocidad;
	}

	public void setSubirVelocidad(JButton subirVelocidad) {
		this.subirVelocidad = subirVelocidad;
	}

	public JButton getBajarVelocidad() {
		return bajarVelocidad;
	}

	public void setBajarVelocidad(JButton bajarVelocidad) {
		this.bajarVelocidad = bajarVelocidad;
	}

	public JLabel getVelocidadActual() {
		return velocidadActual;
	}

	public void setVelocidadActual(JLabel velocidadActual) {
		this.velocidadActual = velocidadActual;
	}
	
	
}
