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
		System.out.println(db.connect());
		
		HashMap<String, String> hm = new HashMap<String, String>(); //make artist hashmap
		hm.put("artist_name", "blah");
		hm.put("nationality", "USA");
		hm.put("artist_type", "band");
		
		System.out.println(db.saveData("artists", hm)); //insert artist
		
		db.closeConnection(); //close db connection
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
