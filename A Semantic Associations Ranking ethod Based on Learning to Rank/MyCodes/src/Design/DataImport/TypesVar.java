package Design.DataImport;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class TypesVar {

	public static Set<String> typs = new HashSet<String>();
	public static Set<String> tpcs = new HashSet<String>();
	public static Map<String,Integer> clas = new HashMap<String,Integer>();
	public static Integer all = 0;
	public static Integer clasCnt = 0;
	public static Vector<Integer>[] dir = new Vector[5005];
	public static String[] clasName = new String[5005];
	
	public static void main(String[] args) {
		all = 0;
		typs.clear();tpcs.clear();clas.clear();
		for(Integer i=0; i<=5000; ++i)
		{
			dir[i] = new Vector<Integer>();
			clasName[i] = "";
		}
		clasCnt = 1;
		clas.put("http://www.w3.org/2002/07/owl#Thing", 1);
		clasName[1] = "http://www.w3.org/2002/07/owl#Thing";
	}

}
