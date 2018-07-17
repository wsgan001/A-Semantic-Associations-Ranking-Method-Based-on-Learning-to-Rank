package Design.DataImport;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import Design.DbHelper;
import Design.DataImport.TypesVar;

public class AddTopic{

	static final String TopicName = "./datas/DBpedia/topical_concepts_en.ttl";
	static final String MapName = "./datas/DBpedia/mappingbased_objects_en.ttl";
	
	private static String sub,pre,obj;
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private static Integer all = 0;
	private static Set<String> alreadyAdded = new HashSet<String>();
	
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
	 
	 private static Boolean judge(String str)
	 {
		 return str.contains("type") && (!str.contains("Electrification"));
	 }
	 
	 private static void addTopic(String filenm)
	 {
		 try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filenm)));
	           ) {
	               String str;
	               while ((str = bufferedReader.readLine()) != null) {
	            	   if(!str.startsWith("<"))continue;
	                   solve(str);
	                   sub = checkString(sub);
	                   pre = checkString(pre);
	                   obj = checkString(obj);
	                   if(sub.contains("http://dbpedia.org/resource") && !sub.contains("Category:"))
	                	   TypesVar.tpcs.add(sub);
	              }
		 		}catch (IOException e) {
	        	   System.out.println("err:   " + sub + "   " + obj);
	               e.printStackTrace();
	           }
		 System.out.println("Topic Cnt : " + TypesVar.tpcs.size());
	 }
	 
		public static void add(String filenm) {
			// TODO Auto-generated method stub
	        sub = "";pre = "";obj = "";
	        String sql = "INSERT INTO topics (NO, subject, object) ";
	        sql = sql + "VALUES(?, ?, ?);";
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
	                   if(TypesVar.tpcs.contains(sub) && !alreadyAdded.contains(sub))
	                   {
	                	   all ++;
	                	   preparedStatement.setInt(1, all);
		                   preparedStatement.setString(2, sub);
		                   preparedStatement.setString(3, sub);
		                   preparedStatement.addBatch();
		                   alreadyAdded.add(sub);
	                   }
	                   if(all % 1000000 == 0)
	                   {
	                       preparedStatement.executeBatch();
	                       dbHelper.getConn().commit();
	                       preparedStatement.clearBatch();
	                       System.out.println(all + "  Time : " + mat.format(new Date()));
	                   }
	                   if(!judge(pre) || sub.equals(obj))continue;
	                   if(TypesVar.tpcs.contains(obj))
	                   {
		                   all ++;
		                   preparedStatement.setInt(1, all);
		                   preparedStatement.setString(2, sub);
		                   preparedStatement.setString(3, obj);
		                   preparedStatement.addBatch();
	                   }
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
	        System.out.println("All : " + all);
	       }
	
	public static void main(String[] args)
	{
		System.out.println("Topic Import Begin!" + "  Time : " + mat.format(new Date()));
		alreadyAdded.clear();
		addTopic(TopicName);
		add(MapName);
		System.out.println("Topic Import End!  " + "  Time : " + mat.format(new Date()));
	}
}