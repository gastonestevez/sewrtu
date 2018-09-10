package entidades;

public class Tacometro extends Medicion {

	private boolean encendido;
	
	public Tacometro(InstrumentoID id, double valorDeMedicion) {
		super(id, valorDeMedicion);
		setEncendido(false);
	}

	public boolean isEncendido() {
		return encendido;
	}

	public void setEncendido(boolean encendido) {
		this.encendido = encendido;
	}

}
