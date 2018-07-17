package Design.DataImport;

import java.sql.*;
import java.sql.Statement;
import java.text.*;
import java.util.Date;
import Design.DbHelper;

public class Create{

	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	public static void add() {
		// TODO Auto-generated method stub
        try (
        		DbHelper dbHelper = new DbHelper()
        	) {
        		//dbHelper.getConn().setAutoCommit(false);
        		Statement st = dbHelper.getConn().createStatement();
        		
        		st.execute("DROP TABLE IF EXISTS mapping");
        		st.execute("DROP TABLE IF EXISTS types");
        		st.execute("DROP TABLE IF EXISTS topics");
        		st.execute("DROP TABLE IF EXISTS class");
        		
        		st.execute("CREATE TABLE `mapping` (\r\n" + 
        				"	`NO` INT NOT NULL,\r\n" + 
        				"	`subject` VARCHAR(250) NOT NULL,\r\n" + 
        				"	`predicate` VARCHAR(70) NOT NULL,\r\n" + 
        				"	`object` VARCHAR(900) NOT NULL,\r\n" + 
        				"	PRIMARY KEY (`NO`)\r\n" + 
        				")\r\n" + 
        				"CHARACTER SET = 'utf8'\r\n" +
        				"COLLATE='utf8_general_ci'\r\n" + 
        				";");
        		
        		st.execute("CREATE TABLE `types` (\r\n" + 
        				"	`NO` INT NOT NULL,\r\n" + 
        				"	`subject` VARCHAR(900) NOT NULL,\r\n" + 
        				"	`predicate` VARCHAR(70) NOT NULL,\r\n" + 
        				"	`object` VARCHAR(900) NOT NULL,\r\n" + 
        				"	PRIMARY KEY (`NO`)\r\n" + 
        				")\r\n" + 
        				"CHARACTER SET = 'utf8'\r\n" +
        				"COLLATE='utf8_general_ci'\r\n" + 
        				";");
        		
        		st.execute("CREATE TABLE `topics` (\r\n" + 
        				"	`NO` INT NOT NULL,\r\n" + 
        				"	`subject` VARCHAR(250) NOT NULL,\r\n" + 
        				"	`object` VARCHAR(900) NOT NULL,\r\n" + 
        				"	PRIMARY KEY (`NO`)\r\n" + 
        				")\r\n" + 
        				"CHARACTER SET = 'utf8'\r\n" +
        				"COLLATE='utf8_general_ci'\r\n" + 
        				";");
        		
        		st.execute("CREATE TABLE `class` (\r\n" + 
        				"	`NO` INT NOT NULL,\r\n" + 
        				"	`class` VARCHAR(900) NOT NULL,\r\n" + 
        				"	`depth` INT NOT NULL,\r\n" + 
        				"	`cnts` INT NOT NULL,\r\n" + 
        				"	PRIMARY KEY (`NO`)\r\n" + 
        				")\r\n" + 
        				"CHARACTER SET = 'utf8'\r\n" + 
         				"COLLATE='utf8_general_ci'\r\n" + 
        				";");
        		
        		st.close();
           }catch (SQLException e) {
               e.printStackTrace();
           }
       }
	
	public static void main(String[] args)
	{
		System.out.println("Create Begin!" + "  Time : " + mat.format(new Date()));
		add();
		System.out.println("Create End!  " + "  Time : " + mat.format(new Date()));
	}
}