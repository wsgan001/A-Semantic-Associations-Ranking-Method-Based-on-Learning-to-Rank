package OtherCodes;

import java.io.*;
import java.sql.*;
import java.text.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.*;
import java.util.*;
import java.util.Date;
import Design.GraphParameters;
import org.apache.jena.query.*;
import Design.DbHelper;

public class Look {

	static final String queryName = "./datas/DBpedia/mappingbased_objects_en.ttl";
	
	private static String sub,pre,obj;
	private static SimpleDateFormat mat = new SimpleDateFormat("现在时间:yyyy年MM月dd日E HH时mm分ss秒");
	private static Integer all = 0;
	
	private static void solve(String s)
	{
		String[] tmp = new String[10];
		tmp = s.split(" ");
		sub = tmp[0].substring(1, tmp[0].length() - 1);
		pre = tmp[1].substring(1, tmp[1].length() - 1);
		obj = tmp[2].substring(1, tmp[2].length() - 1);
	}
	
	 public static String checkString(String str){  
	        String returnStr = "";  
	        if(str.indexOf("'") != -1){//判断字符串是否含有单引号  
	            returnStr = str.replace("'", "''");  
	            str = returnStr;  
	        }  
	        return str;  
	    }
	 
	public static void add() {
		// TODO Auto-generated method stub
        sub = "";pre = "";obj = "";
        Integer cnt = 0;
        String sql = "INSERT INTO mapping (NO, subject, predicate, object) ";
        sql = sql + "VALUES(?, ?, ?, ?);";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(queryName)));
        		DbHelper dbHelper = new DbHelper()
           ) {
        		dbHelper.getConn().setAutoCommit(false);
        		PreparedStatement preparedStatement = dbHelper.getConn().prepareStatement(sql);
               bufferedReader.readLine();
               String str;
               while ((str = bufferedReader.readLine()) != null) {
            	   if(!str.startsWith("<"))continue;
                   solve(str);cnt ++;
                   sub = checkString(sub);
                   pre = checkString(pre);
                   obj = checkString(obj);
                   if(sub.length() > 249 || pre.length() > 249 || obj.length() > 249)
                   {
                	   System.out.println("主语 : " + sub);
                	   System.out.println("谓语 : " + pre);
                	   System.out.println("宾语 : " + obj);
                   }
                   preparedStatement.setInt(1, cnt);
                   preparedStatement.setString(2, sub);
                   preparedStatement.setString(3, pre);
                   preparedStatement.setString(4, obj);
                   preparedStatement.addBatch();
                   if(cnt % 1000000 == 0)
                   {
                       preparedStatement.executeBatch();
                       dbHelper.getConn().commit();
                       preparedStatement.clearBatch();
                       System.out.println(cnt + "  Time : " + mat.format(new Date()));
                   }
               }
               if(cnt % 1000000 > 0)
               {
            	   preparedStatement.executeBatch();
            	   dbHelper.getConn().commit();
               }
               preparedStatement.close();
           } catch (IOException e) {
        	   System.out.println("err:   " + sub + "   " + obj);
               e.printStackTrace();
           } catch (SQLException e) {
        	   System.out.println("err:   " + sub + "   " + obj);
               e.printStackTrace();
           }
        System.out.println("All : " + cnt);
       }
	
	public static void main(String[] args)
	{
		add();
	}
}