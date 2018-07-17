package Design.DataImport;

import java.sql.*;
import java.sql.Statement;
import java.text.*;
import java.util.Date;
import Design.DbHelper;

public class AddIndex{

	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	public static void add() {
		// TODO Auto-generated method stub
        try (
        		DbHelper dbHelper = new DbHelper()
        	) {
        		//dbHelper.getConn().setAutoCommit(false);
        		Statement st = dbHelper.getConn().createStatement();
        		
        		st.execute("ALTER TABLE mapping ADD INDEX `sub`(`subject`);");
        		System.out.println("ALTER TABLE mapping ADD INDEX `sub`(`subject`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE mapping ADD INDEX `obj`(`object`);");
        		System.out.println("ALTER TABLE mapping ADD INDEX `obj`(`object`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE mapping ADD INDEX `pre`(`predicate`);");
        		System.out.println("ALTER TABLE mapping ADD INDEX `pre`(`predicate`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE types ADD INDEX `sub`(`subject`);");
        		System.out.println("ALTER TABLE types ADD INDEX `sub`(`subject`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE types ADD INDEX `obj`(`object`);");
        		System.out.println("ALTER TABLE types ADD INDEX `obj`(`object`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE mapping ADD INDEX `subpre`(`subject`, `predicate`);");
        		System.out.println("ALTER TABLE mapping ADD INDEX `subpre`(`subject`, `predicate`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE mapping ADD INDEX `preobj`(`predicate`, `object`);");
        		System.out.println("ALTER TABLE mapping ADD INDEX `preobj`(`predicate`, `object`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE topics ADD INDEX `sub`(`subject`);");
        		System.out.println("ALTER TABLE topics ADD INDEX `sub`(`subject`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE topics ADD INDEX `obj`(`object`);");
        		System.out.println("ALTER TABLE topics ADD INDEX `obj`(`object`);" + "   " + mat.format(new Date()));
        		st.execute("ALTER TABLE class ADD INDEX `class`(`class`);");
        		System.out.println("ALTER TABLE classdepth ADD INDEX `class`(`class`);" + "   " + mat.format(new Date()));
        		
        		st.close();
           }catch (SQLException e) {
               e.printStackTrace();
           }
       }
	
	public static void main(String[] args)
	{
		System.out.println("AddIndex Begin!" + "  Time : " + mat.format(new Date()));
		add();
		System.out.println("AddIndex End!  " + "  Time : " + mat.format(new Date()));
	}
}