package comunicaciones;

import java.sql.*;

public class ConectorMysql {

	private double ira;
	private double irb;
	private double ambiente;
	private double cilindro;
	private double sew;
	private int vacio;
	private int paradadeemergencia;
	
	public ConectorMysql(double ira, double irb, double ambiente, double cilindro, double sew, int vacio,
			int paradadeemergencia) {
		super();
		this.ira = ira;
		this.irb = irb;
		this.ambiente = ambiente;
		this.cilindro = cilindro;
		this.sew = sew;
		this.vacio = vacio;
		this.paradadeemergencia = paradadeemergencia;
	}
	
	public void insertData(){
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		final String DB_URL = "jdbc:mysql://localhost/medtest";
		
		final String USER = "gaston";
		final String pwd = "database";
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Conectando..");
		try {
			conn = DriverManager.getConnection(DB_URL,USER,pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Insertando...");
		String query = "INSERT INTO mediciones (ira,irb,ambiente,cilindro,sew,vacio,paradadeemergencia) "
				+ "values (?,?,?,?,?,?,?)";
		PreparedStatement prepareStmt;
		try {
			prepareStmt = conn.prepareStatement(query);
			prepareStmt.setDouble(1, getIra());
			prepareStmt.setDouble(2, getIrb());
			prepareStmt.setDouble(3, getAmbiente());
			prepareStmt.setDouble(4, getCilindro());
			prepareStmt.setDouble(5, getSew());
			prepareStmt.setInt(6, getVacio());
			prepareStmt.setInt(7, getParadadeemergencia());
			
			prepareStmt.execute();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printData(){
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		final String DB_URL = "jdbc:mysql://localhost/medtest";
		
		final String USER = "gaston";
		final String pwd = "database";
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Conectando..");
		try {
			conn = DriverManager.getConnection(DB_URL,USER,pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Consulta...");
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "SELECT * FROM mediciones";
		try {
			ResultSet rs;
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String ira = rs.getString("ira");
				String irb = rs.getString("irb");
				System.out.println("ira: "+ira+ " -- irb: "+ irb+ " ..." );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public double getIra() {
		return ira;
	}

	public void setIra(double ira) {
		this.ira = ira;
	}

	public double getIrb() {
		return irb;
	}

	public void setIrb(double irb) {
		this.irb = irb;
	}

	public double getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(double ambiente) {
		this.ambiente = ambiente;
	}

	public double getCilindro() {
		return cilindro;
	}

	public void setCilindro(double cilindro) {
		this.cilindro = cilindro;
	}

	public double getSew() {
		return sew;
	}

	public void setSew(double sew) {
		this.sew = sew;
	}

	public int getVacio() {
		return vacio;
	}

	public void setVacio(int vacio) {
		this.vacio = vacio;
	}

	public int getParadadeemergencia() {
		return paradadeemergencia;
	}

	public void setParadadeemergencia(int paradadeemergencia) {
		this.paradadeemergencia = paradadeemergencia;
	}
	
	

}
