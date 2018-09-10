package ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import handler.Handler;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import config.Configuracion;

public class PanelConfig extends JPanel{

	private static final long serialVersionUID = -1609861030221743615L;
	private String[] puertoStrings = {"COM12","COM1","COM2","COM3","COM4","COM5","COM6","COM7","COM8","COM9"}; //"/dev/ttyACM0","/dev/ttyACM1","/dev/ttyACM2","/dev/ttyACM3","/dev/ttyACM4"};
	private Integer[] tempVector = {25,50,100,125,15000};
	private Handler handler;
	private JComboBox<String> listaPuertos;
	private JComboBox<Integer> listaTemp;
	private static PanelConfig panelConfig;
	static Logger log = Logger.getLogger(PanelConfig.class.getName());
	Font fuente = new Font("Arial", Font.BOLD, 14);

	private PanelConfig(Handler handler){
		setHandler(handler);
		createUI();
	}

	public static PanelConfig getInstance(Handler handler){
		if(panelConfig == null){
			panelConfig = new PanelConfig(handler);
		}
		return panelConfig;
	}
	
	private void createUI() {
		setLayout(new GridLayout(3, 2));
		JLabel puerto = new JLabel("Puerto Arduino: ");
		puerto.setFont(fuente);
		add(puerto);
		setListaPuertos( new JComboBox<String>(puertoStrings));
		getListaPuertos().setSelectedIndex(2);
		add(getListaPuertos());
		
		JLabel limiteTemp = new JLabel("Temperatura limite de Alarma PT100 (°C) : ");
		limiteTemp.setFont(fuente);
		add(limiteTemp);
		setListaTemp(new JComboBox<Integer>(tempVector));
		getListaTemp().setSelectedIndex(2);
		add(getListaTemp());
		
		/*JLabel enviarMail = new JLabel("Enviar mail checkbox");
		enviarMail.setFont(fuente);
		add(enviarMail);*/
		
		/*JLabel seguridadMotor = new JLabel("Seguridad Motor checkBox");
		seguridadMotor.setFont(fuente);
		add(seguridadMotor);*/
		
		JButton valoresDefault = new JButton("Valores Default");
		valoresDefault.setFont(fuente);
		valoresDefault.setSize(100, 100);
		add(valoresDefault);
		
		JButton aplicar = new JButton("Aplicar cambios");
		aplicar.setFont(fuente);
		add(aplicar);
		
		valoresDefault.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getHandler().getConfig().setPuertoArduino("COM9");
				getHandler().getConfig().setTemperaturaLimite(150);
				getListaPuertos().setSelectedIndex(2);
				getListaTemp().setSelectedIndex(2);
				
			}
		});
		
		aplicar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getHandler().getConfig().setPuertoArduino((String)listaPuertos.getSelectedItem());
				getHandler().getConfig().setTemperaturaLimite((Integer)listaTemp.getSelectedItem());
				
				log.info("Nueva configuracion aplicada: "+Configuracion.getInstance().toString());
				JOptionPane.showMessageDialog(null,"CAMBIOS APLICADOS","Configuracion",JOptionPane.INFORMATION_MESSAGE);
			}
		});		
	}	
	
	public JComboBox<String> getListaPuertos() {
		return listaPuertos;
	}

	public void setListaPuertos(JComboBox<String> listaPuertos) {
		this.listaPuertos = listaPuertos;
	}

	public JComboBox<Integer> getListaTemp() {
		return listaTemp;
	}

	public void setListaTemp(JComboBox<Integer> listaTemp) {
		this.listaTemp = listaTemp;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
}
