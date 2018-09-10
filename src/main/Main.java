package main;
import org.apache.log4j.Logger;
import handler.Handler;

public class Main {
	static Logger log = Logger.getLogger(Main.class.getName());
	public static void main(String[] args) {	
		new Thread(new Runnable() {
			@Override
			public void run() {
				log.info("Inicia programa");
				new Handler().init();	
			}
		}).start();		
	}
}
