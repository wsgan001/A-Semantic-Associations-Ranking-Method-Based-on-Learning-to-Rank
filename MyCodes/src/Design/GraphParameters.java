package Design;

import java.util.*;
import java.io.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.query.*;

public class GraphParameters {

	// database
	static private final String dbname = "./datas/dbpedia.nt";
	static private Map<RDFNode, Integer> dbmp_edge = new HashMap<RDFNode, Integer>();
	static private Map<RDFNode, Integer> dbmp_node = new HashMap<RDFNode, Integer>();
	static private Integer dbEdgeAll;
	static private Integer dbEdgeCnt;
	static private Integer dbNodeCnt;
	static private Integer[] dbEdgeNum = new Integer[32005];
	//static private RDFNode[] dbEdgePro = new RDFNode[32005];
	
	//Type of Entities
	static private Map<RDFNode, Integer> typ = new HashMap<RDFNode, Integer>();
	static private Vector<Integer>[] nodeType = new Vector[41];
	static private Integer typeCnt;
	static private Integer[] vis = new Integer[5001];
	static private RDFNode[] typeCollection = new RDFNode[5001];
	
	// SPARQL
	static private Model dbmodel;
	static private InputStream dbopen;
	static private Query dbquery;
	static private QueryExecution dbqe;
	static private ResultSet dbres;
	
	// each Graph
	static private Vector<Integer>[] outEdge = new Vector[41];
	static private RDFNode[] nodeName = new RDFNode[41];     //label to RDFNode
	static private RDFNode[] edgeName = new RDFNode[41];     //label to Property
	static private Integer[] edgeEnd = new Integer[41];      //edge's another Node label
	static private Integer[] edgePro = new Integer[41];      //edge's Property label
	static private Integer[] edgeDir = new Integer[41];      //1-tail to head && 2-head to tail
	//static private Integer[] outCnt = new Integer[41];
	//static private Integer[] inCnt = new Integer[41];
	static private Integer edgeCnt, nodeCnt, allEdge;
	static private Map<RDFNode, Integer> edg = new HashMap<RDFNode, Integer>();
	static private Map<RDFNode, Integer> nod = new HashMap<RDFNode, Integer>();
	static private Boolean[] query = new Boolean[41];
	static private Integer[] edgeOccurTimes = new Integer[41];
	
	//result parameters
	static public double _Size;
	static public double _Freq;
	static public double _Cent;
	static public double _Rinf;
	static public double _Einf;
	static public double _Spec;
	static public double _Rhet;
	static public double _Ehom;
	
	private static void getDataBase()
	{
		
		dbmodel = ModelFactory.createDefaultModel();  
		  
        dbopen = FileManager.get().open(dbname);  
        if (dbopen == null)   
        {  
            throw new IllegalArgumentException("File: " + dbname + " not found");  
        } 
        
        dbmodel.read(dbopen, "", "N3");
        
        StmtIterator iter = dbmodel.listStatements();
        
        while (iter.hasNext())   
        {  
            Statement stmt = iter.nextStatement(); // get next statement  
            //Resource subject = stmt.getSubject(); // get the subject  
            //Property predicate = stmt.getPredicate(); // get the predicate  
            //RDFNode object = stmt.getObject(); // get the object  
  
            RDFNode pred = stmt.getPredicate(); // get the predicate  
            dbEdgeAll ++;	
            if( !dbmp_edge.containsKey(pred) )
            {
            	dbEdgeCnt ++;
            	dbmp_edge.put(pred, dbEdgeCnt);
            }
            
            dbEdgeNum[ dbmp_edge.get(pred) ] ++;
            
            RDFNode sub = stmt.getSubject();
            if( !dbmp_node.containsKey(sub) )
            {
            	dbNodeCnt ++;
            	dbmp_node.put(sub, dbNodeCnt);
            }
            RDFNode obj = stmt.getObject();
            if( !dbmp_node.containsKey(obj) )
            {
            	dbNodeCnt ++;
            	dbmp_node.put(obj, dbNodeCnt);
            }
        }
        
	}
	
	public static void initialize()
	{
		//all
		edgeCnt = nodeCnt = allEdge = typeCnt = 0;
		for(Integer i=0; i<=20; ++i)
		{
			outEdge[i] = new Vector<Integer>();
			nodeType[i] = new Vector<Integer>();
		}
		edg.clear();nod.clear();typ.clear();
		// parameters
		_Size = 0;
		_Freq = 0;
		_Cent = 0;
		_Rinf = 0;
		_Einf = 0;
		_Spec = 0;
		_Rhet = 0;
		_Ehom = 0;
	}
	
	public static void clear()
	{
		//all
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			outEdge[i].clear();
			nodeType[i].clear();
			nodeName[i] = null;
			//inCnt[i] = outCnt[i] = 0;
			query[i] = false;
		}
		for(Integer i=1; i<=edgeCnt; ++i)
		{
			edgeName[i] = null;
			edgeOccurTimes[i] = 0;
		}
		for(Integer i=1; i<=typeCnt; ++i)
		{
			vis[i] = 0;
			typeCollection[i] = null;
		}
		for(Integer i=1; i<=allEdge; ++i)edgeEnd[i] = edgePro[i] = 0;
		edg.clear();nod.clear();typ.clear();
		edgeCnt = nodeCnt = allEdge = typeCnt = 0;
		
		// parameters
		_Size = 0;
		_Freq = 0;
		_Cent = 0;
		_Rinf = 0;
		_Einf = 0;
		_Spec = 0;
		_Rhet = 0;
		_Ehom = 0;
	}
	
	private static Integer getNodeNumber(RDFNode nam)
	{
		if( !nod.containsKey(nam) )
		{
			nodeCnt ++;
			nodeName[nodeCnt] = nam;
			nod.put(nam, nodeCnt);
		}
		return (Integer)nod.get(nam);
	}
	
	private static Integer getEdgeNumber(Property nam)
	{
		if( !edg.containsKey(nam) )
		{
			edgeCnt ++;
			edgeName[edgeCnt] = nam;
			edg.put(nam, edgeCnt);
		}
		return (Integer)edg.get(nam);
	}
	
	public static void addEdge(RDFNode subj, Property pred, RDFNode obj)
	{
		//System.out.println(subj + " " + pred + " " + obj);
		Integer subnum = getNodeNumber(subj);
		Integer objnum = getNodeNumber(obj);
		Integer prenum = getEdgeNumber(pred);
		edgeOccurTimes[prenum] ++;
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
	
	private static void addQueryEntity(RDFNode nam)
	{
		query[getNodeNumber(nam)] = true;
		//System.out.println("Query Entity : " + nam + " && Number. " + getNodeNumber(nam));
	}
	
	private static Integer count(ResultSet tmp)
	{
		Integer ret = 0;
		while(tmp.hasNext())
		{
			tmp.next();
			ret ++;
		}
		return ret;
	}
	
	/*
	private static void calDegree()
	{
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			Enumeration<Integer> ite = outEdge[i].elements();
			while(ite.hasMoreElements())
			{
				Integer now = (Integer)ite.nextElement();
				if(edgeDir[ edgePro[now] ] == 2)
					inCnt[i] ++;
				else
					outCnt[i] ++;
			}
		}
	}
	*/
	
	private static void getType()
	{
		String querystr = "";
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			querystr = "SELECT ?typ WHERE {<" + nodeName[i].toString() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?typ}";
			dbquery = QueryFactory.create(querystr);
			dbqe = QueryExecutionFactory.create(dbquery, dbmodel);
			dbres = dbqe.execSelect();
			while(dbres.hasNext())
			{
				QuerySolution tmp = dbres.next();
				RDFNode now = tmp.get("obj");
				if( !typ.containsKey(now) )
				{
					typeCnt ++;
					typ.put(now, typeCnt);
				}
				nodeType[i].add((Integer)typ.get(now));
			}
		}
	}
	
	private static Integer cal_Size()
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
		return maxdepth - 1;
	}
	
	private static double cal_Freq()
	{
		String querystr = "";
		double ret = 0;
		for(Integer i=1; i<=allEdge; i+=2)
		{
			Integer cntHead = 0, cntTail = 0, cntRel1 = 0, cntRel2 = 0;
			    
			RDFNode head = nodeName[ edgeEnd[i] ];
			RDFNode tail = nodeName[ edgeEnd[i+1] ];
			RDFNode pred = edgeName[ edgePro[i] ];	
			
			// rfout
			querystr = "SELECT ?pre ?obj WHERE {<" + tail.toString() +"> ?pre ?obj}";
			dbquery = QueryFactory.create(querystr);
			dbqe = QueryExecutionFactory.create(dbquery, dbmodel);
			dbres = dbqe.execSelect();
			while(dbres.hasNext())
			{
				QuerySolution tmp = dbres.next();
				cntTail ++;
				RDFNode tmpPre = tmp.get("pre");
				if( tmpPre.equals(pred) ) cntRel1 ++;
			}
				
			// rfin
			querystr = "SELECT ?sub ?pre WHERE {?sub ?pre <" + head.toString() +">}";
			dbquery = QueryFactory.create(querystr);
			dbqe = QueryExecutionFactory.create(dbquery, dbmodel);
			dbres = dbqe.execSelect();
			while(dbres.hasNext())
			{
				QuerySolution tmp = dbres.next();
				cntHead ++;
				RDFNode tmpPre = tmp.get("pre");
				if( tmpPre.equals(pred) ) cntRel2 ++;
			}
			
			ret += ((double)cntRel1 / (double)cntHead + (double)cntRel2 / (double)cntTail) / 2.0;
		}
		return ret / ((double)allEdge / 2.0);
	}
	
	private static double cal_Cent()
	{
		Integer cnt = 0, sum = 0;
		String querystr = "";
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			if( query[i] )continue;
			String now = "<" + nodeName[i].toString() + ">";
			cnt ++;
			querystr = "SELECT ?sub ?pre WHERE {?sub ?pre " + now +"}";
			dbquery = QueryFactory.create(querystr);
			dbqe = QueryExecutionFactory.create(dbquery, dbmodel);
			dbres = dbqe.execSelect();
			sum += count(dbres);
			querystr = "SELECT ?pre ?obj WHERE {" + now +" ?pre ?obj}";
			dbquery = QueryFactory.create(querystr);
			dbqe = QueryExecutionFactory.create(dbquery, dbmodel);
			dbres = dbqe.execSelect();
			sum += count(dbres);
		}
		if( cnt == 0 )return 0;else return (double)sum / (double)cnt;
	}
	
	private static double cal_Rinf()
	{
		double edges = (double)dbEdgeCnt;
		double fenmu = Math.log(edges);
		double sum = 0;
		for(int i=1; i<=allEdge; ++i)
		{
			if( edgeDir[i] == 2 ) continue;
			sum += Math.log(edges / (double)dbEdgeNum[ dbmp_edge.get(edgePro[i]) ]);
		}
		sum /= fenmu;
		return sum / ((double)allEdge / 2.0);
	}
	
	private static double cal_Einf()
	{
		double nodes = dbNodeCnt;
		double fenmu = Math.log(nodes);
		double ret = 0;
		String askstr = "";
		Integer cnt = 0;
		for(Integer i=1; i<=nodeCnt; ++i)
		{
			if( query[i] )continue;
			cnt ++;
			Integer mi = 32767;
			Enumeration<Integer> ite = nodeType[i].elements();
        	while(ite.hasMoreElements())
			{
				Integer tmp = ite.nextElement();
				askstr = "SELECT ?sub WHERE { ?sub <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <" + typeCollection[tmp].toString() + ">}";
				dbquery = QueryFactory.create(askstr);
				dbqe = QueryExecutionFactory.create(dbquery, dbmodel);
				dbres = dbqe.execSelect();
				mi = Math.min(mi, count(dbres));
			}
			ret += Math.log(nodes / (double)mi );
		}
		ret /= fenmu;
		return ret / (double)cnt;
	}
	
	private static double cal_Spec()
	{
		
		return 0;
	}
	
	private static double cal_Rhet()
	{
		return (double)edgeCnt / ((double)allEdge / 2.0);
	}
	
	private static double cal_Ehom()
	{
		Integer cnt = 0;
		double ret = 0;
		for(Integer i=1; i<nodeCnt; ++i)
		{
			Integer cnti = 0;
			Enumeration<Integer> itei = nodeType[i].elements();
        	while(itei.hasMoreElements())
        	{
        		Integer nowi = (Integer)itei.nextElement();
        		vis[nowi] = i; cnti ++;
        	}
			for(Integer j=i+1; j<=nodeCnt; ++j)
			{
				cnt ++;
				Integer cntj = 0, cntt = 0;
				Enumeration<Integer> itej = nodeType[j].elements();
	        	while(itej.hasMoreElements())
	        	{
	        		Integer nowj = (Integer)itej.nextElement();
	        		cntj ++;
	        		if( vis[nowj].equals(i) )cntt ++;
	        	}
	        	ret += (double)cntt / (double)(cnti + cntj - cntt);
			}
		}
		return ret / (double)cnt;
	}
	
	public static void calall()
	{
		_Size = cal_Size();
		_Freq = cal_Freq();
		_Cent = cal_Cent();
		_Rinf = cal_Rinf();
		getType();
		_Einf = cal_Einf();
		_Spec = cal_Spec();
		_Rhet = cal_Rhet();
		_Ehom = cal_Ehom();
	}
	
	public static void solve()
	{
		
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		getDataBase();
		initialize();
		solve();
	}

}
