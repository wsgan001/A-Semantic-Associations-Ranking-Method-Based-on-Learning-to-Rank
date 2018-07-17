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

public class RM09 {
	
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
	
	private static double D_r = 0;
	private static double L_r = 0;
	private static double F_r = 0;

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
	
	private static void Lr()
	{
		L_r = (double)edgeCnt / 2.0 + (double)nodeCnt;
		L_r = 1.0 / L_r;
	}
	
	private static void Dr()
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
			D_r = 0;
		else
		{
			double d = 0.05;
			D_r = 0;
			double llr = nodeCnt;
			for(Integer i=1; i<=topicCnt; ++i)
				D_r += d + (1.0 - d) * (topics[i] / llr);
			D_r *= (1.0 - (double)cc / llr);
			D_r /= (double)nodeCnt;
		}
		try {
			dbh.close();pst.close();rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void Fr()
	{
		double mai = 0, mao = 0;
		F_r = 0;
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			mai = Math.max(mai, inCnt[i]);
			mao = Math.max(mai, outCnt[i]);
		}
		for(Integer i=1; i<=nodeCnt; ++i)
			F_r += (inCnt[i]/mai + outCnt[i]/mao) / 2.0;
		F_r /= (double)nodeCnt;
	}
	
	private static void calAll()
	{
		CalDegree();
		Dr();Lr();Fr();
	}
	
	private static void pri(String filenm)
	{
		File opf = new File(filenm);
		DecimalFormat fm=new DecimalFormat("#.00000000");
		try {
			FileWriter fw = new FileWriter(opf);
			fw.write(fm.format(D_r));fw.write(" ");
			fw.write(fm.format(L_r));fw.write(" ");
			fw.write(fm.format(F_r));fw.write(" ");
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
