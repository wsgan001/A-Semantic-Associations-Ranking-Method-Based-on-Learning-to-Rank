package Design.CalGenTrainTest;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;
import Design.CalGenTrainTest.ParaImportance;
import Design.UserClustering.SVMDataGen;
import Design.UserClustering.TrainandTest;

public class ParaImporImple {

	private static DecimalFormat fm=new DecimalFormat("#0.00000000");
	
	private static Integer k = 5;//see
	private static Integer label = 1;//para

	private static void cal(Vector<Double> u)
	{
		double ma = 0, mi = 1, sig = 0, miu = 0;
		double len = (double)u.size();
		for(Double p : u)miu += p / 100.0;
		miu /= len;
		for(Double p : u)
		{
			ma = Math.max(ma, p/100.0);
			mi = Math.min(mi, p/100.0);
			sig += (p/100.0 - miu) * (p/100.0 - miu);
		}
		sig /= len;
		sig = Math.sqrt(sig);
		System.out.println("Max :   \t" + fm.format(ma));
		System.out.println("Min :   \t" + fm.format(mi));
		System.out.println("Miu :   \t" + fm.format(miu));
		System.out.println("Sigma : \t" + fm.format(sig));
		for(Double p : u)System.out.print(fm.format(p / 100.0) + ", ");
		System.out.println();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ParaImportance.main(label);
		SVMDataGen.main(args);
		
		try {
			Vector<Double> x = TrainandTest.main(k);
			cal(x);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
