package my.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility methods for executing database queries
 * @author Karissa Tuason
 *
 */
public class DBUtilities {
	/**
	 * Executes database update.
	 * @param con the database connection
	 * @param query the query
	 * @return 1 if success, 0 otherwise.
	 */
	public static int executeUpdate(Connection con, String query) {
		int rs;
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeUpdate(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
