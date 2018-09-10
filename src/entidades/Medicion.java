package entidades;

public class Medicion {
	
	private InstrumentoID id;
	private double valorDeMedicion;
	private String descripcion;
	
	
	
	public Medicion(InstrumentoID id, double valorDeMedicion) {
		this.id = id;
		this.valorDeMedicion = valorDeMedicion;
	}
	
	public InstrumentoID getId() {
		return id;
	}
	public void setId(InstrumentoID id) {
		this.id = id;
	}
	public double getValorDeMedicion() {
		return valorDeMedicion;
	}
	public void setValorDeMedicion(double valorDeMedicion) {
		this.valorDeMedicion = valorDeMedicion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
