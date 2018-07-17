package Design.CalGenTrainTest;

import java.io.IOException;  
import Design.CalGenTrainTest.svm_predict;  
import Design.CalGenTrainTest.svm_train;  
  
public class TrainandTest {  
  
    /** 
     * @param args 
     * @throws IOException 
     */  
	private static String[] ret = new String[31];
	
    public static void main(String[] args) throws IOException {  
        // TODO Auto-generated method stub
    	for(Integer id=1; id<=30; ++id)
    	{
	    	String tt = id.toString() + ".txt";
	    	
	    	svm_scale s1= new svm_scale();
	    	String[] s1arg = {"./datas/SVM/Train/TrainScale_" + tt, "./datas/SVM/Train/Train_" + tt};
	    	s1.main(s1arg);
	       
	        svm_train t = new svm_train();
	        String[] targ = { //"-s","0","-t","2",
	        		"-q",
	        		"./datas/SVM/Train/TrainScale_" + tt, 
	                "./datas/SVM/Model/Model_" + tt };
	        t.main(targ);
	        
	        svm_scale s2= new svm_scale();
	    	String[] s2arg = {"./datas/SVM/Test/TestDataScale_" + tt, "./datas/SVM/Test/TestData_" + tt};
	    	s2.main(s2arg);
	        
	        svm_predict p = new svm_predict();
	        String[] parg = { "./datas/SVM/Test/TestDataScale_" + tt, 
	                "./datas/SVM/Model/Model_" + tt, 
	                "./datas/SVM/Test/TestAnsw_" + tt };
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
    	for(Integer id=1; id<=30; ++id)
    	{
    		System.out.println("ID : " + id + "   RESULT : " + ret[id]);
    		for(Integer i=1; i<=10; ++i)
    			if(ret[id].contains(i.toString() + "0.0%"))
    			{
    				cnt ++;
    				tj[i] ++;
    			}
    	}
    	tj[0] = 30-cnt;
    	for(Integer i=0; i<=10; ++i)System.out.println(i.toString() + "0.0%" + "  :  " + tj[i]);
    }  
  
} 
