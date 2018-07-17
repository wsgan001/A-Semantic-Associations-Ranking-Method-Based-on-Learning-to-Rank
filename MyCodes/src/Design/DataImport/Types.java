package Design.DataImport;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import Design.DbHelper;
import Design.DataImport.TypesVar;

public class Types{

	static final String TypeName1 = "./datas/DBpedia/instance_types_en.ttl";
	static final String TypeName2 = "./datas/DBpedia/instance_types_transitive_en.ttl";
	static final String thing = "http://www.w3.org/2002/07/owl#Thing";
	
	private static String sub,pre,obj;
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
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
	        if(str.indexOf("'") != -1){ 
	            returnStr = str.replace("'", "''");  
	            str = returnStr;  
	        }  
	        return str;  
	    }
	 
	public static void add(String filenm) {
		// TODO Auto-generated method stub
        sub = "";pre = "";obj = "";
        Integer ma1 = 0, ma2 = 0, ma3 = 0;
        String sql = "INSERT INTO types (NO, subject, predicate, object) ";
        sql = sql + "VALUES(?, ?, ?, ?);";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filenm)));
        		DbHelper dbHelper = new DbHelper()
           ) {
        		dbHelper.getConn().setAutoCommit(false);
        		PreparedStatement preparedStatement = dbHelper.getConn().prepareStatement(sql);
               String str;
               while ((str = bufferedReader.readLine()) != null) {
            	   if(!str.startsWith("<"))continue;
                   solve(str);
                   sub = checkString(sub);
                   pre = checkString(pre);
                   obj = checkString(obj);
                   if(!TypesVar.clas.containsKey(obj))continue;
                   all ++;
                   ma1 = Math.max(ma1, sub.length());
                   ma2 = Math.max(ma2, pre.length());
                   ma3 = Math.max(ma3, obj.length());
                   preparedStatement.setInt(1, all);
                   preparedStatement.setString(2, sub);
                   preparedStatement.setString(3, pre);
                   preparedStatement.setString(4, obj);
                   preparedStatement.addBatch();
                   if(obj.equals(thing))TypesVar.typs.add(sub);
                   if(all % 1000000 == 0)
                   {
                       preparedStatement.executeBatch();
                       dbHelper.getConn().commit();
                       preparedStatement.clearBatch();
                       System.out.println(all + "  Time : " + mat.format(new Date()));
                   }
               }
               if(all % 1000000 > 0)
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
        System.out.println("All : " + all + "   " + ma1 + "   " + ma2 + "   " + ma3);
       }
	
	public static void main(String[] args)
	{
		System.out.println("Types Import Begin!" + "  Time : " + mat.format(new Date()));
		all = TypesVar.all;
		add(TypeName1);
		add(TypeName2);
		TypesVar.all = all;
		System.out.println("Types Import End!  " + "  Time : " + mat.format(new Date()));
	}
}