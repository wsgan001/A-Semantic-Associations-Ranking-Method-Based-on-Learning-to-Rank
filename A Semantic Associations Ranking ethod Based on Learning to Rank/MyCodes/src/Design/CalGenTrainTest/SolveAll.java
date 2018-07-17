package Design.CalGenTrainTest;

import Design.CalGenTrainTest.CalParameters;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Design.CalGenTrainTest.DataGenerateStandard;
import Design.CalGenTrainTest.TestLabelGen;
import Design.CalGenTrainTest.TrainandTest;

public class SolveAll {

	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("CalGenTrainTest Begin!" + mat.format(new Date()));
		
		try {
			CalParameters.main(args);
			//TestLabelGen.main(args);
			DataGenerateStandard.main(args);
			TrainandTest.main(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("CalGenTrainTest End!" + mat.format(new Date()));
	}

}
