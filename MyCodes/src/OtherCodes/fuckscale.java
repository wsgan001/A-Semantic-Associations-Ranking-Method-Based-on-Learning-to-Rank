package OtherCodes;

import java.io.IOException;

import Design.CalGenTrainTest.svm_scale;

public class fuckscale {

	private static String prefix = "./datas/Test/";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		svm_scale s1= new svm_scale();
    	String[] s1arg = {prefix + "DataScale_1.txt", prefix + "Data_1.txt"};
    	try {
			s1.main(s1arg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
