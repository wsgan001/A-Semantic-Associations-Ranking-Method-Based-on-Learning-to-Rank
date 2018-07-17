package Design;

import Design.DataImport.All;

import java.text.SimpleDateFormat;
import java.util.Date;

import Design.CalGenTrainTest.SolveAll;

public class BeginWithNothing {

	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("AllThings Begin!" + mat.format(new Date()));
		
		All.main(args);
		SolveAll.main(args);
		
		System.out.println("AllThings End!" + mat.format(new Date()));
	}

}
