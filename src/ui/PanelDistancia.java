package ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import handler.Handler;

public class PanelDistancia extends JPanel{

	private static final long serialVersionUID = 9109680021535300082L;
	private static PanelDistancia panelDistancia;
	private Handler handler;
	Font fuente = new Font("Arial", Font.BOLD, 160);
	private Timer timer;
	private JLabel valorDistancia;

	
	private PanelDistancia(Handler handler) {
		setHandler(handler);
		createUI();
	}
	 
	private void createUI() {
		// TODO Auto-generated method stub
		setLayout(new GridLayout(2, 1));
		JLabel lDistancia = new JLabel("DISTANCIA");
		lDistancia.setFont(fuente);
		add(lDistancia);
		 valorDistancia = new JLabel("0 cm");
		valorDistancia.setFont(fuente);
		add(valorDistancia);
		setTimer(new Timer(getHandler().getConfig().getTimerVisualizacion(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<String> mediciones = getHandler().getMedicionesBO().getListaDeMediciones();
				if(new Integer(mediciones.get(0)) > 350){
					valorDistancia.setText("Fuera de rango :)");
				}else{
					valorDistancia.setText(mediciones.get(0) + " cm");
				}
			}
		}));
		getTimer().start();
	}

	public static PanelDistancia getInstance(Handler handler){
		if(panelDistancia == null){
			panelDistancia = new PanelDistancia(handler);
		}
		return panelDistancia;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

}
