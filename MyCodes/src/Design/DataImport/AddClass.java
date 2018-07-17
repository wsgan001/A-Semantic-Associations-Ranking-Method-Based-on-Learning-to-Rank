package Design.DataImport;

import java.io.*;
import java.text.*;
import java.util.Date;
import Design.DataImport.TypesVar;


public class AddClass{

	static final String ClasName = "./datas/DBpedia/dbpedia_2015-10.nt";
	
	private static String sub,pre,obj;
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private static Integer all = 0;
	//static private Vector<Integer>[] outEdge = new Vector[8005];
	
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
        String str = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filenm)));
           ) {
               while ((str = bufferedReader.readLine()) != null) {
            	   if(!str.startsWith("<"))continue;
                   solve(str);
                   sub = checkString(sub);
                   pre = checkString(pre);
                   obj = checkString(obj);
                   //if(!pre.contains("subClassOf"))continue;
                   if(sub.contains("dbpedia.org/ontology"))
                   {
                	   if(!TypesVar.clas.containsKey(sub))
                	   {
                		   TypesVar.clasCnt ++;
                		   TypesVar.clasName[TypesVar.clasCnt] = sub;
                		   TypesVar.clas.put(sub, TypesVar.clasCnt);
                	   }
                   }
                   if(obj.contains("dbpedia.org/ontology"))
                   {
                	   if(!TypesVar.clas.containsKey(obj))
                	   {
                		   TypesVar.clasCnt ++;
                		   TypesVar.clasName[TypesVar.clasCnt] = obj;
                		   TypesVar.clas.put(obj, TypesVar.clasCnt);
                	   }
                   }
                   all ++;
                   if(
                		   (pre.contains("rdf-schema#domain") || pre.contains("rdf-schema#subClassOf") || pre.contains("rdf-schema#range")) 
                		   && (sub.contains("dbpedia.org/ontology") || sub.equals("http://www.w3.org/2002/07/owl#Thing")) 
                		   && (obj.contains("dbpedia.org/ontology") || obj.equals("http://www.w3.org/2002/07/owl#Thing")) 
                	 )
                	   TypesVar.dir[TypesVar.clas.get(obj)].add(TypesVar.clas.get(sub));
               }
           } catch (IOException e) {
        	   System.out.println("err:   " + sub + "   " + obj);
               e.printStackTrace();
           }
        System.out.println("All : " + all);
       }
	
	public static void main(String[] args)
	{
		System.out.println("Class Import Begin!" + "  Time : " + mat.format(new Date()));
		add(ClasName);
		System.out.println("Class Import End!  " + "  Time : " + mat.format(new Date()));
	}
}