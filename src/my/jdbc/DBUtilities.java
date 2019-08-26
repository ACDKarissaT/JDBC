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
	 * @throws DBExceptions 
	 */
	public static int executeUpdate(Connection con, String query) throws DBExceptions {
		int rs;
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeUpdate(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DBExceptions(e.getMessage());
		} catch (Exception e) {
			throw new DBExceptions(e.getMessage());
		}
	}
}
