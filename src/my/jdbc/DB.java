package my.jdbc;

import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * This class connects and inserts to a database given properties.
 * @author Karissa Tuason
 * @since 1.0
 */
public class DB {
	/**
	 * database connection.
	 */
	private Connection con;
	/**
	 * Properties of database connection
	 */
	private Properties prop;
	
	/**
	 * Constuctor. Initiates the database properties using properties instance prop.
	 * @param prop <p>This is the properties instance. Must have "driver_type", "driver_path", "database_address", "database_name", "user", and  "pass"
	 * For example: 	driver_type=mysql driver_path=C:\\path\\to\\Driver.jar database_address=127.0.0.1:3306 database_name=databaseName user=username	pass=password
	 * </p>
	 */
	public DB(Properties prop) {
		this.prop = prop;
	}
	
	/**
	 * Attempts to connect to database using properties.
	 * @return true if connection is successful, false otherwise.
	 * @throws DBExceptions throws if there is an error.
	 */
	public boolean connect() throws DBExceptions {
		con = DBConnection.getDBInstance(prop);
		return con != null;
	}
	
	/**
	 * Sees if connection is open
	 * @return true if open false if closed
	 */
	public boolean isOpen() {
		try {
			return !(con.isClosed());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * if connection is open, closes the connection
	 * @return returns true;
	 */
	public boolean closeConnection() {
		if (isOpen()) {
			DBConnection.closeConnection();
		}
		return true;
	}
	
	/**
	 * Given the tablename and Hashmap of column_name to column_value, creates a query and attempt to insert to database.
	 * @param tableName the name of the table to insert into.
	 * @param hm the hashmap of the column_name -> value pair.
	 * @return "Success" if success, "failure" if failed
	 */
	public String saveData(String tableName,HashMap<String,String> hm) {
		String query = "INSERT INTO {{table}} ({{columns}}) VALUES ({{values}});";
		query = query.replace("{{table}}", tableName);
		StringBuilder columns = new StringBuilder("");
		StringBuilder values = new StringBuilder("");
		Set<String> keys = hm.keySet();
		for (String string : keys) {
			if(columns.length() == 0) {
				columns.append("`"+string+"`");
				values.append("\'"+ hm.get(string)+"\'");
			}else {
				columns.append(", `"+ string + "`");
				values.append(", \'"+ hm.get(string)+"\'");
			}
		}
		query = query.replace("{{columns}}", columns.toString());
		query = query.replace("{{values}}", values.toString());
		return saveData(query);
	}
	
	/**
	 * Given a query runs query on the database.
	 * @param query the query
	 * @return "Success" if successful, "failure" if failed.
	 */
	public String saveData(String query) {
		int num = DBUtilities.executeUpdate(con, query);
		if (num == 0) {
		return "failure";
		} else {
			return "Success";
		}
	}
	
}
