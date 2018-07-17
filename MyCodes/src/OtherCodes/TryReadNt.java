package OtherCodes;

import java.io.*;

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.*;
import java.util.*;
import Design.GraphParameters;
import org.apache.jena.query.*;

public class TryReadNt 
{

	static String prefix = "./datas/Size";
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		Integer num = 10;
		String filenmh = prefix + "_" + num.toString() + "_" + "xh.nt";
		String filenml = prefix + "_" + num.toString() + "_" + "xl.nt";
		String queryName = prefix + "_" + num.toString() + "_" + "entities.txt";
		
		Model model = ModelFactory.createDefaultModel();  
		  
        InputStream in = FileManager.get().open(filenml);  
        if (in == null)   
        {  
            throw new IllegalArgumentException("File: " + filenml + " not found");  
        } 
        
        model.read(in, "", "N3");
        
        StmtIterator iter = model.listStatements();  
        
        RDFNode t1 = null,t2 = null,t3 = null,tt = null;
        
        String cs = "http://dbpedia.org/ontology/bandMember";
        // print out the predicate, subject and object of each statement  
        while (iter.hasNext())   
        {  
            Statement stmt = iter.nextStatement(); // get next statement   
            RDFNode predicate = stmt.getPredicate(); // get the predicate  
            t1 = stmt.getSubject();
        	t2 = stmt.getPredicate();
        	t3 = stmt.getObject();
            if( predicate.toString().equals(cs) ) break;
        }  
        
        String querystr = "SELECT ?pre WHERE {<" + t1.toString() +"> ?pre <" + t3.toString() +">}";
        System.out.println(querystr);
        Query query = QueryFactory.create(querystr);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet ret = qe.execSelect();
        while(ret.hasNext())
        {
        	QuerySolution tmp = ret.next();
        	tt = tmp.get("pre");
        }
        
        if( t2.equals(tt) )System.out.println("Yes!" + t2.toString());else System.out.println("Fuck!");
	}

}
