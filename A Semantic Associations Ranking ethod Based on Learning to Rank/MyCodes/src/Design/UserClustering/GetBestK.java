package Design.UserClustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import KMeansCluster.Process;

public class GetBestK {

	private static Process model = new Process();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			model.loadData("./datas/UserVector.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Entry<ArrayList<Double>, ArrayList<Double>> ret = model.cluster(1, 31);
		ArrayList<Double> tmp1 = ret.getKey();
		ArrayList<Double> tmp2 = ret.getValue();
		for(Integer i=1; i<=30; ++i)
		{
			System.out.print(i + " : ");
			System.out.print(tmp1.get(i-1) + "\t\t\t");
			System.out.println(tmp2.get(i-1));
		}
	}

}
