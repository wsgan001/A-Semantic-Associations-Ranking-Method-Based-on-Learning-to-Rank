package Design.CalGenTrainTest;

import java.io.*;
import java.sql.*;
import java.text.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.FileManager;
import java.util.*;
import java.util.Date;
import Design.DbHelper;

public class CalParameters {
	
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy骞碝M鏈坉d鏃� HH:mm:ss");
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
	
	private static double tf_num = 0;
	private static double tf_max = 0;
	private static double tf_min = 0;
	private static double tf_miu = 0;
	private static double tf_sigma = 0;
	private static double rinf = 0;
	private static double einf = 0;
	private static double spec = 0;
	private static double ehom = 0;
	private static double rhet = 0;
	private static double La = 0;
	private static double _size = 0;
	static private double PFF_miu;
	static private double PFF_sigma;
	static private double PFF_ma;
	static private double PFF_mi;
	static private double PF_miu;
	static private double PF_sigma;
	static private double PF_ma;
	static private double PF_mi;
	static private double du = 0;
	
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
		La = _size = rhet = ehom = spec = rinf = einf = du = 0;
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
	
	private static void Du()
	{
		double mai = 0, mao = 0;
		du = 0;
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			mai = Math.max(mai, inCnt[i]);
			mao = Math.max(mai, outCnt[i]);
		}
		Integer cnt = 0;
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			if(query[i])continue;
			cnt ++;
			du += (inCnt[i]/mai + outCnt[i]/mao) / 2.0;
		}
		if(cnt == 0)du = 0;else du /= (double)cnt;
	}
	
	private static void Size()
	{
		Integer[] dis = new Integer[41];
        for(Integer i=1; i<=nodeCnt; ++i)dis[i] = 0;
        Integer bgnd = 0, maxdepth = 0;
        for(Integer i=1; i<=nodeCnt; ++i)
        {
        	if( outEdge[i].size() > 1 )continue;
        	bgnd = i;
        	break;
        }
        dis[bgnd] = 1;
        Queue<Integer> tmp = new LinkedList<Integer>();
        tmp.offer(bgnd);
        while(tmp.peek() != null)
        {
        	Integer x = tmp.poll();
        	if( dis[x] > maxdepth )
        	{
        		maxdepth = dis[x];
        		bgnd = x;
        	}
        	Enumeration<Integer> ite = outEdge[x].elements();
        	while(ite.hasMoreElements())
        	{
        		Integer now = (Integer)ite.nextElement();
        		now = edgeEnd[now];
        		if( dis[now] == 0)
        		{
        			dis[now] = dis[x] + 1;
        			tmp.offer(now);
        		}
        	}
        }
        for(Integer i=1; i<=nodeCnt; ++i)dis[i] = 0;
        maxdepth = 0;dis[bgnd] = 1;
        tmp.clear();tmp.offer(bgnd);
        while(tmp.peek() != null)
        {
        	Integer x = tmp.poll();
        	if( dis[x] > maxdepth )
        	{
        		maxdepth = dis[x];
        		bgnd = x;
        	}
        	Enumeration<Integer> ite = outEdge[x].elements();
        	while(ite.hasMoreElements())
        	{
        		Integer now = (Integer)ite.nextElement();
        		now = edgeEnd[now];
        		if( dis[now] == 0)
        		{
        			dis[now] = dis[x] + 1;
        			tmp.offer(now);
        		}
        	}
        }
		_size = maxdepth - 1;
	}
	
	private static void AssociationLength()
	{
		La = (double)allEdge / 2.0;
	}
	
	private static void TopicFeatures()
	{
		DbHelper dbh = new DbHelper();
		String sql = "SELECT object FROM topics WHERE subject=?;";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = dbh.getConn().prepareStatement(sql);
			for(Integer i=1; i<=nodeCnt; ++i)
			{
				if(query[i])continue;
				pst.setString(1, checkString(nodeName[i].toString()));
				rs = pst.executeQuery();
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
			tf_num = tf_max = tf_min = tf_miu = tf_sigma = 0;
		else
		{
			tf_num = topicCnt;
			tf_max = 0;tf_min = 1000000;tf_sigma = 0;tf_miu = 0;
			for(Integer i=1; i<=topicCnt; ++i)
			{
				double x = (double)topics[i] / (double)(La + 1);
				tf_max = Math.max(tf_max, x);
				tf_min = Math.min(tf_min, x);
				tf_miu += x;
			}
			tf_miu /= tf_num;
			for(Integer i=1; i<=topicCnt; ++i)
			{
				double x = (double)topics[i] / (double)(La + 1);
				double tmp = x - tf_miu;
				tf_sigma += tmp * tmp / tf_num;
			}
			tf_sigma = Math.sqrt(tf_sigma);
		}
		try {
			dbh.close();pst.close();rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void PropertyFrequencyFeatures()
	{
		double[] tmp = new double[41];
		for(Integer i=1; i<=40; ++i) tmp[i] = 0;
		PFF_ma = 0;PFF_mi = 5;PFF_miu = 0;PFF_sigma = 0;
		DbHelper dbh1 = new DbHelper();
		String sql1 = "SELECT COUNT(*) AS nums FROM mapping WHERE (subject=?) and (predicate=?);";
		PreparedStatement pst1 = null;
		ResultSet rs1 = null;
		DbHelper dbh2 = new DbHelper();
		String sql2 = "SELECT COUNT(*) AS nums FROM mapping WHERE (predicate=?) and (object=?);";
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		try {
			pst1 = dbh1.getConn().prepareStatement(sql1);
			pst2 = dbh2.getConn().prepareStatement(sql2);
			for(Integer i=1; i<=allEdge; i+=2)
			{
				String ex = null, ey = null, ee = null;
				double fin = 0, fout = 0;
				double exOut = 0, eyIn = 0, pIn = 0, pOut = 0;
				ex = nodeName[ edgeEnd[i+1] ];
				ey = nodeName[ edgeEnd[i] ];
				ee = edgeName[ edgePro[i] ];
				//pOut
				pst1.setString(1, ex);
				pst1.setString(2, ee);
				rs1 = pst1.executeQuery();
				if(rs1.next())pOut = rs1.getDouble("nums");
				//pIn
				pst2.setString(1, ee);
				pst2.setString(2, ey);
				rs2 = pst2.executeQuery();
				if(rs2.next())pIn = rs2.getDouble("nums");
				//exOut
				exOut = outCnt[ edgeEnd[i+1] ];
				/*
				pst.setString(1, ex);
				pst.setString(2, "%");
				pst.setString(3, "%");
				rs = pst.executeQuery();
				if(rs.next())exOut = rs.getDouble("nums");
				*/
				
				//exIn
				eyIn = inCnt[ edgeEnd[i] ];
				/*
				pst.setString(1, "%");
				pst.setString(2, "%");
				pst.setString(3, ey);
				rs = pst.executeQuery();
				if(rs.next())eyIn = rs.getDouble("nums");
				*/
				if(exOut > 0)fout = pOut / exOut;
				if(eyIn > 0)fin = pIn / eyIn;
				tmp[ (i+1) >> 1 ] = fin + fout;
				PFF_miu += fin + fout;
				/*
				System.out.println(((i+1)>>1) + ":");
				System.out.println("pOut : " + pOut);
				System.out.println("pIn : " + pIn);
				System.out.println("exOut : " + exOut);
				System.out.println("eyIn : " + eyIn);
				*/
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PFF_miu /= ((double)(allEdge) / 2.0);
		for(Integer i=1; i<=(allEdge>>1); ++i)
		{
			PFF_ma = Math.max(PFF_ma, tmp[i]);
			PFF_mi = Math.min(PFF_mi, tmp[i]);
			double now = tmp[i] - PFF_miu;
			PFF_sigma += now*now / ((double)(allEdge) / 2.0);
		}
		PFF_sigma = Math.sqrt(PFF_sigma);
		try {
			dbh1.close();pst1.close();rs1.close();
			dbh2.close();pst2.close();rs2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void PopularityFeatures()
	{
		double[] tmp = new double[41];
		for(Integer i=1; i<=40; ++i) tmp[i] = 0;
		PF_ma = 0;PF_mi = 5000000;PF_miu = 0;PF_sigma = 0;
		Integer cnt = 0;
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			tmp[i] = inCnt[i] + outCnt[i];
			if(query[i])continue;
			cnt ++;
			PF_miu += tmp[i];
			//System.out.println(nodeName[i].toString() + " : " + tmp[i]);
		}
		if(cnt == 0)
		{
			PF_mi = 0;
			return;
		}
		PF_miu /= (double)cnt;
		for(Integer i=1; i<=(allEdge>>1); ++i)
		{
			if(query[i])continue;
			PF_ma = Math.max(PF_ma, tmp[i]);
			PF_mi = Math.min(PF_mi, tmp[i]);
			double now = tmp[i] - PF_miu;
			PF_sigma += now*now / (double)cnt;
		}
		PF_sigma = Math.sqrt(PF_sigma);
	}
	
	private static void RHET()
	{
		rhet = (double)edgeCnt / ((double)allEdge / 2.0);
	}
	
	private static void EHOM()
	{
		Map<String, Integer> sol = new HashMap<String, Integer>();
		Integer tmpcnt = 0;
		Integer[] coun = new Integer[105];
		for(Integer i=1; i<=100; ++i) coun[i] = 0;
		DbHelper dbh = new DbHelper();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "SELECT object FROM types WHERE subject=?;";
		try {
			pst = dbh.getConn().prepareStatement(sql);
			for(Integer i=1; i<nodeCnt; ++i)
				for(Integer j=i+1; j<=nodeCnt; ++j)
				{
					//clear
					for(Integer k=1; k<=tmpcnt; ++k)coun[k] = 0;
					tmpcnt = 0;sol.clear();
					//solve
					pst.setString(1, nodeName[i]);
					rs = pst.executeQuery();
					while(rs.next())
					{
						String tmp = rs.getString("object");
						Integer id = 0;
						if(sol.containsKey(tmp))id = sol.get(tmp);
						else
						{
							tmpcnt ++;
							sol.put(tmp, tmpcnt);
							id = tmpcnt;
						}
						coun[id] ++;
					}
					pst.setString(1, nodeName[j]);
					rs = pst.executeQuery();
					while(rs.next())
					{
						String tmp = rs.getString("object");
						Integer id = 0;
						if(sol.containsKey(tmp))id = sol.get(tmp);
						else
						{
							tmpcnt ++;
							sol.put(tmp, tmpcnt);
							id = tmpcnt;
						}
						coun[id] ++;
					}
					Integer fk = 0;
					for(Integer k=1; k<=tmpcnt; ++k)
						if(coun[k] > 1)fk ++;
					ehom += (double)fk / (double)tmpcnt;
					if(tmpcnt == 0)System.out.println(nodeName[i] + "   " + nodeName[j]);
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
		Integer fm = (nodeCnt * (nodeCnt - 1)) >> 1;
		ehom /= (double)fm;
	}
	
	private static void SPEC()
	{
		DbHelper dbh1 = new DbHelper();
		String sql1 = "SELECT depth FROM class WHERE class=?;";
		PreparedStatement pst1 = null;
		ResultSet rs1 = null;
		DbHelper dbh2 = new DbHelper();
		String sql2 = "SELECT object FROM types WHERE subject=?;";
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		Integer cnt = 0, sum = 0;
		try {
			pst1 = dbh1.getConn().prepareStatement(sql1);
			pst2 = dbh2.getConn().prepareStatement(sql2);
			for(Integer i=1; i<=nodeCnt; ++i)
			{
				if(query[i])continue;
				cnt ++;
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
					if(x < 10000)ma = Math.max(ma, x);
				}
				sum += ma;
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
		if(cnt>0)spec = (double)sum / (double)cnt;
		spec = spec / (double)maxDepth;
	}

	private static void RINF()
	{
		DbHelper dbh = new DbHelper();
		String sql = "SELECT COUNT(*) AS nums FROM mapping WHERE predicate=?;";
		PreparedStatement pst = null;
		ResultSet rs = null;
		double sum = 0, sir = 0;
		try {
			pst = dbh.getConn().prepareStatement(sql);
			for(Integer i=1; i<=edgeCnt; i+=2)
			{
				pst.setString(1, edgeName[i]);
				rs = pst.executeQuery();
				if(rs.next())sir = rs.getDouble("nums");
				sum -= Math.log((sir) / (double)maxEdge);
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
		sum /= ((double)edgeCnt / 2.0);
		sum /= Math.log((double)maxEdge);
		rinf = sum;
	}
	
	private static void EINF()
	{
		DbHelper dbh1 = new DbHelper();
		String sql1 = "SELECT object FROM types WHERE subject=?;";
		PreparedStatement pst1 = null;
		ResultSet rs1 = null;
		DbHelper dbh2 = new DbHelper();
		String sql2 = "SELECT cnts FROM class WHERE class=?;";
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		Integer cnt = 0;
		double sum = 0;
		try {
			pst1 = dbh1.getConn().prepareStatement(sql1);
			pst2 = dbh2.getConn().prepareStatement(sql2);
			for(Integer i=1; i<=nodeCnt; ++i)
			{
				if(query[i])continue;
				cnt ++;
				Integer mi = 30000000;
				pst1.setString(1, nodeName[i]);
				rs1 = pst1.executeQuery();
				while(rs1.next())
				{
					String typ = rs1.getString("object");
					pst2.setString(1, typ);
					rs2 = pst2.executeQuery();
					Integer x = 30000000;
					if(rs2.next())x = rs2.getInt("cnts");
					mi = Math.min(mi, x);
				}
				sum -= Math.log((double)mi / (double)maxNode);
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
		if(cnt>0)einf = sum / (double)cnt;
		einf = einf / Math.log((double)maxNode);
	}
	
	private static void calAll()
	{
		Size();
		//System.out.println("Size End!  Time : " + mat.format(new Date()));
		AssociationLength();
		//System.out.println("AssociationLength End!  Time : " + mat.format(new Date()));
		TopicFeatures();
		//System.out.println("TopicFeatures End!  Time : " + mat.format(new Date()));
		CalDegree();
		//System.out.println("CalDegree End!  Time : " + mat.format(new Date()));
		Du();
		PropertyFrequencyFeatures();
		//System.out.println("StringFrequencyFeatures End!  Time : " + mat.format(new Date()));
		PopularityFeatures();
		//System.out.println("PopularityFeatures End!  Time : " + mat.format(new Date()));
		RHET();
		//System.out.println("RHET End!  Time : " + mat.format(new Date()));
		EHOM();
		//System.out.println("EHOM End!  Time : " + mat.format(new Date()));
		SPEC();
		//System.out.println("SPEC End!  Time : " + mat.format(new Date()));
		RINF();
		//System.out.println("RINF End!  Time : " + mat.format(new Date()));
		EINF();
		//System.out.println("EINF End!  Time : " + mat.format(new Date()));
	}
	
	private static void pri(String filenm)
	{
		File opf = new File(filenm);
		DecimalFormat fm=new DecimalFormat("#.00000000");
		try {
			FileWriter fw = new FileWriter(opf);
			//SPEC
			fw.write(fm.format(spec));fw.write(" ");
			//RHET
			fw.write(fm.format(rhet));fw.write(" ");
			//EHOM
			fw.write(fm.format(ehom));fw.write(" ");
			//SIZE
			fw.write(fm.format(_size));fw.write(" ");
			//RINF
			fw.write(fm.format(rinf));fw.write(" ");
			//EINF
			fw.write(fm.format(einf));fw.write(" ");
			//Du
			fw.write(fm.format(du));fw.write(" ");
			//Enter
			fw.write("\r\n");
			//TopicFeatures
			fw.write(fm.format(tf_num));fw.write(" ");
			fw.write(fm.format(tf_max));fw.write(" ");
			fw.write(fm.format(tf_min));fw.write(" ");
			fw.write(fm.format(tf_miu));fw.write(" ");
			fw.write(fm.format(tf_sigma));fw.write("\r\n");
			//AssociationLength
			fw.write(fm.format(La));fw.write("\r\n");
			//StringFrequencyFeatures
			fw.write(fm.format(PFF_ma));fw.write(" ");
			fw.write(fm.format(PFF_mi));fw.write(" ");
			fw.write(fm.format(PFF_sigma));fw.write(" ");
			fw.write(fm.format(PFF_miu));fw.write("\r\n");
			//PopularityFeatures
			fw.write(fm.format(PF_ma));fw.write(" ");
			fw.write(fm.format(PF_mi));fw.write(" ");
			fw.write(fm.format(PF_sigma));fw.write(" ");
			fw.write(fm.format(PF_miu));fw.write("\r\n");
			//TopicFeatures
			/*fw.write(topicCnt);fw.write("\r\n");
			for(Integer i=1; i<=topicCnt; ++i)
			{
				fw.write(topic[i] + " ");
				fw.write(topics[i]);
				fw.write("\r\n");
			}*/
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
		pri(fm+"_t.txt");
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
