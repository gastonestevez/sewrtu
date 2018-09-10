package entidades;

public class Termometro extends Medicion {

	private int limiteTemperatura;
	
	public Termometro(InstrumentoID id, double valorDeMedicion,int limite) {
		super(id, valorDeMedicion);
		setLimiteTemperatura(limite);
		
	}
	
	public void setLimiteTemperatura(int limite){
		this.limiteTemperatura = limite;
	}
	
	public int getLimiteTemperatura(){
		return this.limiteTemperatura;
	}

}
