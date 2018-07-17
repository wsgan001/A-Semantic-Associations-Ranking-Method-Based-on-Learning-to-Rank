package Design.DataImport;

import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import Design.DbHelper;
import Design.DataImport.TypesVar;

public class AddClassDepCnt{
	
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	 
	private static Integer[] dep = new Integer[5005];
	
	private static void CalculateDepth()
	{
		Queue<Integer> q = new LinkedList<Integer>();
		q.clear();
		for(Integer i=1; i<=5000; ++i)dep[i] = 12345;
		dep[1] = 1;q.offer(1);
		while(!q.isEmpty())
		{
			Integer x = q.poll();
			for(Integer i : TypesVar.dir[x])
			{
				if(dep[i]>10000)q.offer(i);
				dep[i] = Math.min(dep[i], dep[x] + 1);
			}
		}
		Integer cnt = 0;
		for(Integer i=1; i<=TypesVar.clasCnt; ++i)
			if(dep[i] < 10000)cnt ++;
		System.out.println("All Type : " + TypesVar.clasCnt + "      Has Depth : " + cnt);
	}
	
	private static void add() 
	{
		// TODO Auto-generated method stub
	    String sql = "INSERT INTO class (NO, class, depth, cnts) ";
	    sql = sql + "VALUES(?, ?, ?, ?);";
	    DbHelper qsdbh = new DbHelper();
		String qssql = "SELECT COUNT(*) AS nums FROM types WHERE object=?;";
		PreparedStatement qspst = null;
		ResultSet qsrs = null;
	    try
	    {
	    	qspst = qsdbh.getConn().prepareStatement(qssql);
	    	DbHelper dbHelper = new DbHelper();
	        dbHelper.getConn().setAutoCommit(false);
	        PreparedStatement pst = dbHelper.getConn().prepareStatement(sql);
	        
	        for(Integer i=1; i<=TypesVar.clasCnt; ++i)
	        {
	        	qspst.setString(1, TypesVar.clasName[i]);
	        	qsrs = qspst.executeQuery();
	        	Integer p = 0;
	        	if(qsrs.next())p = qsrs.getInt("nums");
	        	pst.setInt(1, i);
	        	pst.setString(2, TypesVar.clasName[i]);
	        	pst.setInt(3, dep[i]);
	        	pst.setInt(4, p);
	        	pst.addBatch();
	        	
	        	//if(i%100 == 0)System.out.println("Solved : " + i + "   " + mat.format(new Date()));
	        }
	        
	        pst.executeBatch();
	        dbHelper.getConn().commit();
	        pst.close();dbHelper.close();
	        qspst.close();qsrs.close();qsdbh.close();
	    }catch (SQLException e) {e.printStackTrace();}
	}
	
	public static void main(String[] args)
	{
		System.out.println("ClassDepCnt Import Begin!" + "  Time : " + mat.format(new Date()));
		for(Integer i=1; i<=5000; ++i)dep[i] = 0;
		CalculateDepth();add();
		System.out.println("ClassDepCnt Import End!  " + "  Time : " + mat.format(new Date()));
	}
}