package Design.UserClustering;

import java.io.IOException;
import java.util.Vector;

import Design.CalGenTrainTest.svm_predict;  
import Design.CalGenTrainTest.svm_train;  
import Design.CalGenTrainTest.svm_scale; 

public class TrainandTest {  
  
    /** 
     * @param args 
     * @throws IOException 
     */  
	private static String[] ret = new String[31];
	
    public static Vector<Double> main(Integer k) throws IOException {  
        // TODO Auto-generated method stub
    	String prefix = "./datas/SVM/Cluster" + k.toString() + "/";
    	for(Integer id=1; id<=k; ++id)
    	{
	    	String tt = id.toString() + ".txt";
	    	
	    	svm_scale s1= new svm_scale();
	    	String[] s1arg = {prefix + "Train/DataScale_" + tt, prefix + "Train/Data_" + tt};
	    	s1.main(s1arg);
	       
	        svm_train t = new svm_train();
	        String[] targ = { //"-s","0","-t","2",
	        		"-q",
	        		prefix + "Train/DataScale_" + tt, 
	                prefix + "Model/Model_" + tt };
	        t.main(targ);
	        
	        svm_scale s2= new svm_scale();
	    	String[] s2arg = {prefix + "Test/DataScale_" + tt, prefix + "Test/Data_" + tt};
	    	s2.main(s2arg);
	        
	        svm_predict p = new svm_predict();
	        String[] parg = { prefix + "Test/DataScale_" + tt, 
	        		prefix + "Model/Model_" + tt, 
	                prefix + "Test/Answ_" + tt };
	        p.main(parg); 
	        
	        ret[id] = p.ret;
    	}
    	System.out.println();
    	System.out.println();
    	System.out.println();
    	System.out.println();
    	System.out.println();
    	Integer[] tj = new Integer[11];
    	for(Integer i=0; i<=10; ++i)tj[i] = 0;
    	Integer cnt = 0;
    	for(Integer id=1; id<=k; ++id)System.out.println("ID : " + id + "   RESULT : " + ret[id]);
    	
    	Vector<Double> pp = new Vector<Double>();
    	pp.clear();
    	for(Integer id=1; id<=k; ++id)
    	{
    		Integer pos1 = 0, pos2 = 0;
    		for(Integer j=0; j<ret[id].length(); ++j)
    		{
    			if(ret[id].charAt(j) == '=')pos1 = j;
    			if(ret[id].charAt(j) == '%')pos2 = j;
    		}
    		String nn = "";
    		for(Integer j=pos1+1; j<pos2; ++j)nn+=ret[id].charAt(j);
    		pp.add(Double.parseDouble(nn));
    	}
    	return pp;
    }  
  
} 
