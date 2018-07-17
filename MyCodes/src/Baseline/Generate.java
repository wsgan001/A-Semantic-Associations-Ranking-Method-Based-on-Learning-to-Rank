package Baseline;

import Baseline.RM05;
import Baseline.RM09;
import Design.CalGenTrainTest.DataGenerateStandard;
import Design.UserClustering.SVMDataGen;

public class Generate {
	public static void main(String[] args)
	{
		//RM05.main(args);
		RM09.main(args);
		DataGenerateStandard.main(args);
		SVMDataGen.main(args);
	}
}
