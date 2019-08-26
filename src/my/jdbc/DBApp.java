package my.jdbc;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * App to test DB. 
 * @author Karissa Tuason
 *
 */
public class DBApp {
	public static void main(String[] args) {
		Properties prop = getProperties(); //makes properties from property file
		
		DB db = new DB(prop);				//create db and connect
		System.out.println("Connecting to database");
		try {
			System.out.println(db.connect());
		} catch (DBExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		HashMap<String, String> hm = new HashMap<String, String>(); //make artist hashmap
		hm.put("artist_name", "blah");
		hm.put("nationality", "USA");
		hm.put("artist_type", "band");
		
		System.out.println("Inserting artist");
		System.out.println(db.saveData("artists", hm)); //insert artist
		
		System.out.println("Insert artist to albums. Should fail.");
		System.out.println(db.saveData("albums", hm));
		
		System.out.println("Closing database connection.");
		System.out.println(db.closeConnection()); //close db connection
	}
	
	/**
	 * Loads properties from file "config.properties".
	 * @return Properties specified by file.
	 */
	public static Properties getProperties() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (Exception e) {
			System.out.println("no properties");
			return null;
		}
		return prop;
	}

}
