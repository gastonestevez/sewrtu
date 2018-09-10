package ui;

import java.awt.Font;
import java.awt.GridLayout;
import handler.Handler;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelAyuda extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font font = new Font("Verdana",Font.BOLD,20);
	
	public PanelAyuda(Handler handler){
		createUI();
	}
	
	private void createUI() {
		setLayout(new GridLayout(4, 1));
		JLabel version = new JLabel("Mediciones Versión 1.8",SwingConstants.CENTER);
		version.setFont(font);
		add(version);
		
		JLabel fecha = new JLabel("06/2015",SwingConstants.CENTER);
		fecha.setFont(font);
		add(fecha);
		
		JLabel contactos = new JLabel("Contactos",SwingConstants.CENTER);
		contactos.setFont(font);
		add(contactos);
		
		JLabel nombres = new JLabel("Gaston Estevez, Nahuel Martinez",SwingConstants.CENTER);
		nombres.setFont(font);
		add(nombres);

	}
	
}
