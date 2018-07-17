package Design;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Vector;

import KMeansCluster.Process;

public class GetCluster_Vec {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Integer k = 5;
		
		Process model = new Process();
		try {
			model.loadData("./datas/UserVector.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Entry<Integer[], Double> ret = model.cluster(k);
		Integer[] ret_label = ret.getKey();
		Integer[] cnt = new Integer[31];
		for(Integer i=0; i<=30; ++i)cnt[i] = 0;
		for(Integer i=0; i<30; ++i)
		{
			cnt[ ret_label[i] ] ++;
			System.out.print((i+1) + ":" + ret_label[i] + "  ");
			if(i%5==4)System.out.println();
		}
		for(Integer i=0; i<k; ++i)System.out.println(i + ":" + cnt[i]);
	}

}
