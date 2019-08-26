package my.jdbc;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * Singleton Class that takes in properties and gives a connection to the database.
 * @author Karissa Tuason
 *
 */
public class DBConnection {
	/**
	 * Driver class definition. Used to load driver.
	 * @author Karissa Tuason
	 *
	 */
	private class DriverShim implements Driver {
	    private Driver driver;
	    DriverShim(Driver d) { this.driver = d; }
	    public boolean acceptsURL(String u) throws SQLException {
	        return this.driver.acceptsURL(u);
	    }
	    public Connection connect(String u, Properties p) throws SQLException {
	        return this.driver.connect(u, p);
	    }
		@Override
		public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
			// TODO Auto-generated method stub
			return this.driver.getPropertyInfo(url, info);
		}
		@Override
		public int getMajorVersion() {
			// TODO Auto-generated method stub
			return this.driver.getMajorVersion();
		}
		@Override
		public int getMinorVersion() {
			// TODO Auto-generated method stub
			return this.driver.getMinorVersion();
		}
		@Override
		public boolean jdbcCompliant() {
			// TODO Auto-generated method stub
			return this.driver.jdbcCompliant();
		}
		@Override
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			// TODO Auto-generated method stub
			return this.driver.getParentLogger();
		}
	}
	
	/**
	 * the instance of this class (Singleton)
	 */
	private static DBConnection con;
	/**
	 * The connection to database.
	 */
	private static Connection connection;
	/**
	 * The database connection properties.
	 */
	private Properties prop = null;
	/**
	 * The driver class name.
	 */
	private String driver;//
	/**
	 * The database connection path
	 */
	private String url;//
	/**
	 * The path of the driver (absolute path recommended).
	 */
	private String jar;//
	/**
	 * username
	 */
	private String user;//
	/**
	 * password
	 */
	private String pass;//
	/**
	 * database type
	 */
	private String dType;
	
	/**
	 * Constructor. Loads properties and connects to the database.
	 * @param prop the connection properties.
	 * @throws DBExceptions 
	 */
	private DBConnection(Properties prop) throws DBExceptions {
		loadProperties(prop);
		connect();
		
	}
	
	/**
	 * Loads driver and connects to the database
	 * @return true if connected, false if not.
	 */
	private boolean connect() {
		URL u = null;
		try {
			u = new URL(jar);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String classname = driver;
		URLClassLoader ucl = new URLClassLoader(new URL[] { u });
		

		try {
			Driver d = (Driver)Class.forName(classname, true, ucl).newInstance();
			DriverManager.registerDriver(new DriverShim(d));
			connection = DriverManager
					.getConnection(url, user, pass);
			return true;
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your JDBC Driver?");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Loads the connection properties.
	 * @param prop the connection properties
	 * @throws DBExceptions 
	 */
	private void loadProperties(Properties prop) throws DBExceptions {
		this.dType = prop.getProperty("driver_type");
		this.jar = DBConstants.jar.replace("{{driverPath}}", prop.getProperty("driver_path"));
		this.pass = prop.getProperty("pass");
		this.user = prop.getProperty("user");
		
		if(dType.equals("mysql")) {
			String dat = prop.getProperty("database_address") + "/" + prop.getProperty("database_name");
			this.url = DBConstants.mySqlUrl.replace("{{database}}", dat);
			this.driver = DBConstants.mySqlDriver;
		}else if(dType.equals("oracle")) {
			String dat = prop.getProperty("database_address") + ":" + prop.getProperty("database_name");
			this.url = DBConstants.oracleUrl.replace("{{database}}", dat);
			this.driver = DBConstants.oracleDriver;
		}else if(dType.equals("postgresql")) {
			String dat = prop.getProperty("database_address") + "/" + prop.getProperty("database_name");
			this.url = DBConstants.postUrl.replace("{{database}}", dat);
			this.driver = DBConstants.postDriver;
		}else if(dType.equals("mssql")) {
			String dat = DBConstants.msUrl.replace("{{address}}", prop.getProperty("database_address"));
			dat = dat.replace("{{database}}", prop.getProperty("database_name"));
			this.url = dat;
			this.driver = DBConstants.msDriver;
		} else {
			throw new DBExceptions("Invalid Property format.");
		}
	}
	
	/**
	 * Creates connection if not already instantiated and gives the connection instance.
	 * @param prop connection properties.
	 * @return connection instance.
	 * @throws DBExceptions throws if there is an error
	 */
	public static Connection getDBInstance(Properties prop) throws DBExceptions {
		if (con == null) {
			con = new DBConnection(prop);
		}
		return connection;
		
	}
	
	/**
	 * closes database connection.
	 */
	public static void closeConnection() {
		try {
			connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
