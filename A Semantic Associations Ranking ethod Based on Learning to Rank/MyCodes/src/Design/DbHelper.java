package Design;

import java.sql.*;

public class DbHelper implements AutoCloseable{
    private static final String url = "jdbc:mysql://localhost:3306/dbpedia?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static final String name = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "lzy199661";

    private Connection conn = null;
    private Statement statement = null;
    public DbHelper() {
        try {
            Class.forName(name);//指定链接类型
            conn = DriverManager.getConnection(url, user, password);//获取链接
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public static String getUrl() {
		return url;
	}

	public static String getName() {
		return name;
	}

	public static String getUser() {
		return user;
	}

	public static String getPassword() {
		return password;
	}

	public void close() {
        try {
            this.statement.close();
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
