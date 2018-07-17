package Baseline;

import java.io.*;
import java.sql.*;
import java.text.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.FileManager;
import java.util.*;
import java.util.Date;
import Design.DbHelper;

public class RM05 {
	
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd�? HH:mm:ss");
	private static final Integer maxDepth = 9;
	private static final Integer maxEdge = 17494749;
	private static final Integer maxNode = 5356354;
	private static Model model;
	
	static private Vector<Integer>[] outEdge = new Vector[41];
	static private String[] nodeName = new String[41];     //label to String
	static private String[] edgeName = new String[41];     //label to String
	static private Integer[] edgeEnd = new Integer[41];      //edge's another Node label
	static private Integer[] edgePro = new Integer[41];      //edge's String label
	static private Integer[] edgeDir = new Integer[41];      //1-tail to head && 2-head to tail
	static private Integer edgeCnt, nodeCnt, allEdge, topicCnt;
	static private Map<String, Integer> edg = new HashMap<String, Integer>();
	static private Map<String, Integer> nod = new HashMap<String, Integer>();
	static private Boolean[] query = new Boolean[41];
	static private Map<String, Integer> tpc = new HashMap<String, Integer>(); 
	static private String[] topic = new String[41];
	static private Integer[] topics = new Integer[41];

	static private double[] outCnt = new double[41];
	static private double[] inCnt = new double[41];
	
	private static double C_a = 0;
	private static double R_a = 0;
	private static double P_a = 0;
	private static double L_a = 0;
	private static double S_a = 0;

	public static void initialize()
	{
		//all
		edgeCnt = nodeCnt = allEdge = topicCnt = 0;
		for(Integer i=0; i<=40; ++i)
		{
			outEdge[i] = new Vector<Integer>();
			query[i] = false;
			topics[i] = 0;
		}
		edg.clear();nod.clear();tpc.clear();
		// parameters
	}
	
	public static void clear()
	{
		//all
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			outEdge[i].clear();
			nodeName[i] = null;
			inCnt[i] = outCnt[i] = 0;
			query[i] = false;
		}
		for(Integer i=1; i<=edgeCnt; ++i)
		{
			edgeName[i] = null;
		}
		for(Integer i=1; i<=topicCnt; ++i)
		{
			topic[i] = null;
			topics[i] = 0;
		}
		for(Integer i=1; i<=allEdge; ++i)edgeEnd[i] = edgePro[i] = 0;
		edg.clear();nod.clear();tpc.clear();
		edgeCnt = nodeCnt = allEdge = topicCnt = 0;
		// parameters
	}
	
	public static String checkString(String str){  
        String returnStr = "";  
        if(str.indexOf("'") != -1){
            returnStr = str.replace("'", "''");  
            str = returnStr;  
        }  
        return str;  
    }
	
	private static void addQueryEntity(String nam)
	{
		query[getNodeNumber(nam)] = true;
		//System.out.println("Query Entity : " + nam + " && Number. " + getNodeNumber(nam));
	}
	
	private static void AddQuery(String filenm)
	{
        File query = new File(filenm); 
        InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(query));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        BufferedReader br = new BufferedReader(reader); 
        String line = "";  
        while (true) {  
            try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} 
            if(line == null)break;
            addQueryEntity(checkString(line.substring(1, line.length() - 1)));
        }
	}
	
	private static Integer getNodeNumber(String nam)
	{
		if( !nod.containsKey(nam) )
		{
			nodeCnt ++;
			nodeName[nodeCnt] = nam;
			nod.put(nam, nodeCnt);
		}
		return (Integer)nod.get(nam);
	}
	
	private static Integer getTopicNumber(String nam)
	{
		if( !tpc.containsKey(nam) )
		{
			topicCnt ++;
			topic[topicCnt] = nam;
			tpc.put(nam, topicCnt);
		}
		return (Integer)tpc.get(nam);
	}
	
	private static Integer getEdgeNumber(String nam)
	{
		if( !edg.containsKey(nam) )
		{
			edgeCnt ++;
			edgeName[edgeCnt] = nam;
			edg.put(nam, edgeCnt);
		}
		return (Integer)edg.get(nam);
	}
	
	public static void addEdge(String subj, String pred, String obj)
	{
		//System.out.println(subj + " " + pred + " " + obj);
		Integer subnum = getNodeNumber(subj);
		Integer objnum = getNodeNumber(obj);
		Integer prenum = getEdgeNumber(pred);
		//edgeOccurTimes[prenum] ++;
		allEdge ++;
		outEdge[subnum].add(allEdge);
		edgeEnd[allEdge] = objnum;
		edgePro[allEdge] = prenum;
		edgeDir[allEdge] = 1;
		
		allEdge ++;
		outEdge[objnum].add(allEdge);
		edgeEnd[allEdge] = subnum;
		edgePro[allEdge] = prenum;
		edgeDir[allEdge] = 2;
	}
	
	private static void CalDegree()
	{
		DbHelper dbh1 = new DbHelper();
		String sql1 = "SELECT COUNT(*) AS nums FROM mapping WHERE subject=?;";
		PreparedStatement pst1 = null;
		ResultSet rs1 = null;
		DbHelper dbh2 = new DbHelper();
		String sql2 = "SELECT COUNT(*) AS nums FROM mapping WHERE object=?;";
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		try {
			pst1 = dbh1.getConn().prepareStatement(sql1);
			pst2 = dbh2.getConn().prepareStatement(sql2);
			for(Integer i=1; i<=nodeCnt; ++i)
			{
				pst1.setString(1, nodeName[i]);
				pst2.setString(1, nodeName[i]);
				rs1 = pst1.executeQuery();
				rs2 = pst2.executeQuery();
				if(rs1.next())outCnt[i] = rs1.getDouble("nums");
				if(rs2.next())inCnt[i] = rs2.getDouble("nums");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbh1.close();pst1.close();rs1.close();
			dbh2.close();pst2.close();rs2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void La()
	{
		L_a = (double)edgeCnt / 2.0 + (double)nodeCnt;
		L_a = 1.0 / L_a;
	}
	
	private static void Ca()
	{
		DbHelper dbh = new DbHelper();
		String sql = "SELECT object FROM topics WHERE subject=?;";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer cc = 0;
		try {
			pst = dbh.getConn().prepareStatement(sql);
			for(Integer i=1; i<=nodeCnt; ++i)
			{
				pst.setString(1, checkString(nodeName[i].toString()));
				rs = pst.executeQuery();
				if(!rs.next())cc ++;
				while(rs.next())
				{
					String tmp = rs.getString("object");
					topics[ getTopicNumber(tmp) ] ++;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(topicCnt == 0)
			C_a = 0;
		else
		{
			C_a = 0;
			double lengtha = nodeCnt;
			for(Integer i=1; i<=topicCnt; ++i)
				C_a += (double)topics[i];
			C_a *= (1.0 - (double)cc / lengtha);
			C_a /= lengtha;
		}
		try {
			dbh.close();pst.close();rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void Sa()
	{
		DbHelper dbh1 = new DbHelper();
		String sql1 = "SELECT depth FROM class WHERE class=?;";
		PreparedStatement pst1 = null;
		ResultSet rs1 = null;
		DbHelper dbh2 = new DbHelper();
		String sql2 = "SELECT object FROM types WHERE subject=?;";
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		S_a = 1;
		try {
			pst1 = dbh1.getConn().prepareStatement(sql1);
			pst2 = dbh2.getConn().prepareStatement(sql2);
			for(Integer i=1; i<=nodeCnt; ++i)
			{
				if(query[i])continue;
				Integer ma = 0;
				pst2.setString(1, nodeName[i]);
				rs2 = pst2.executeQuery();
				while(rs2.next())
				{
					String typ = rs2.getString("object");
					pst1.setString(1, typ);
					rs1 = pst1.executeQuery();
					Integer x = 0;
					if(rs1.next())x = rs1.getInt("depth");
					if(x < 10000)S_a *= (double)x/(double)maxDepth;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbh1.close();pst1.close();rs1.close();
			dbh2.close();pst2.close();rs2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void Pa()
	{
		DbHelper dbh = new DbHelper();
		String sql = "SELECT COUNT(*) AS nums FROM mapping WHERE predicate=?;";
		PreparedStatement pst = null;
		ResultSet rs = null;
		double sum = 0, sir = 0, ma = 0;
		try {
			pst = dbh.getConn().prepareStatement(sql);
			for(Integer i=1; i<=edgeCnt; i+=2)
			{
				pst.setString(1, edgeName[i]);
				rs = pst.executeQuery();
				sir = 0;
				if(rs.next())sir = rs.getDouble("nums");
				ma = Math.max(sir, ma);
				sum += sir;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbh.close();pst.close();rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		P_a = sum / ma;
		P_a /= ((double)edgeCnt / 2.0);
	}
	
	private static void Ra()
	{
		DbHelper dbh1 = new DbHelper();
		String sql1 = "SELECT object FROM types WHERE subject=?;";
		PreparedStatement pst1 = null;
		ResultSet rs1 = null;
		DbHelper dbh2 = new DbHelper();
		String sql2 = "SELECT cnts FROM class WHERE class=?;";
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		double sum = 0;
		try {
			pst1 = dbh1.getConn().prepareStatement(sql1);
			pst2 = dbh2.getConn().prepareStatement(sql2);
			for(Integer i=1; i<=nodeCnt; ++i)
			{
				//if(query[i])continue;
				Integer ma = 0;
				pst1.setString(1, nodeName[i]);
				rs1 = pst1.executeQuery();
				while(rs1.next())
				{
					String typ = rs1.getString("object");
					pst2.setString(1, typ);
					rs2 = pst2.executeQuery();
					Integer x = 30000000;
					if(rs2.next())x = rs2.getInt("cnts");
					if(x < 5919419) ma = Math.max(ma, x);
				}
				sum += ma / (double)5919419;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbh1.close();pst1.close();rs1.close();
			dbh2.close();pst2.close();rs2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		R_a = sum / (double)nodeCnt;
	}
	
	private static void calAll()
	{
		CalDegree();
		La();Ca();Sa();Pa();Ra();
	}
	
	private static void pri(String filenm)
	{
		File opf = new File(filenm);
		DecimalFormat fm=new DecimalFormat("#.00000000");
		try {
			FileWriter fw = new FileWriter(opf);
			fw.write(fm.format(C_a));fw.write(" ");
			fw.write(fm.format(S_a));fw.write(" ");
			fw.write(fm.format(R_a));fw.write(" ");
			fw.write(fm.format(P_a));fw.write(" ");
			fw.write(fm.format(L_a));fw.write(" ");
			fw.write("\r\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(filenm);
	}
	
	private static void ReadFile(String filenm)
	{
		model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open(filenm);  
        if (in == null)   {  throw new IllegalArgumentException("File: " + filenm + " not found");  } 
        model.read(in, "", "N3");
		StmtIterator iter = model.listStatements();
		while (iter.hasNext())   
        {  
            Statement stmt = iter.nextStatement(); // get next statement   
            String sub = checkString(stmt.getSubject().toString());
            String pre = checkString(stmt.getPredicate().toString());
            String obj = checkString(stmt.getObject().toString());
            addEdge(sub, pre, obj);
        }
	}
	
	private static final String prefix = "./datas/RankEval2017/data/";
	private static String[] nm = new String[10];
	
	private static void Solve(String fm)
	{
		System.out.println(fm + " Begin !  Time : " + mat.format(new Date()));
		clear();
		ReadFile(fm+".nt");
		//System.out.println("ReadFile End!  Time : " + mat.format(new Date()));
		calAll();
		pri(fm+".txt");
		System.out.println(fm + " End !  Time : " + mat.format(new Date()));
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		initialize();
		nm[1] = "Centr";
		nm[2] = "EHom";
		nm[3] = "EInf";
		nm[4] = "Freq";
		nm[5] = "RHet";
		nm[6] = "RInf";
		nm[7] = "Size";
		nm[8] = "Spec";
		for(Integer i=1; i<=8; ++i)
			for(Integer j=1; j<=30; ++j)
			{
				String fm = prefix + nm[i] + "_" + j.toString();
				AddQuery(fm + "_entities.txt");
				Solve(fm + "_xh");
				Solve(fm + "_xl");
			}
	}

}
