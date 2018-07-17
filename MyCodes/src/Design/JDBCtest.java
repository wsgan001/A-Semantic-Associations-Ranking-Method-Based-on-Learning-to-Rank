package Design;

import java.sql.*;
import java.util.*;
import Design.JDBCUtils;

public class JDBCtest {
	
	private static Connection connection;
	private static PreparedStatement pst;
	private static ResultSet rs;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "create table instance_types_transitive_en(NO char(20),subject varchar(50),predicate varchar(50),object varchar(50),primary key(NO))";
		connection = JDBCUtils.getConnection();
		if (connection != null)
			try {
				pst = connection.prepareStatement(sql);
				pst.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtils.close(connection, pst, rs);
			}
	}

}
