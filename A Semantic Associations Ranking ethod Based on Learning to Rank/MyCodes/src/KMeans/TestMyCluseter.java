package KMeans;

import java.io.IOException;
import java.util.Vector;

import KMeans.KMeans;
import KMeansCluster.Process;

public class TestMyCluseter {

	private static KMeans m1 = new KMeans();
	private static Process m2 = new Process();
	private static final Integer KK = 5;

	private static void pri(Integer[] ret)
	{
		Vector<Integer>[] ans = new Vector[KK];
		for(Integer i=0; i<KK; ++i)ans[i] = new Vector<Integer>();
		for(Integer i=0; i<30; ++i)ans[ret[i]].add(i);
		for(Integer i=0; i<KK; ++i)
		{
			for(Integer j : ans[i])System.out.print(j + "  ");
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			m1.loadData("./datas/UserVector.txt");
			m2.loadData("./datas/UserVector.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Integer[] ret1 = m1.Solve(KK);
		Integer[] ret2 = m2.cluster(KK).getKey();
		pri(ret1);
		System.out.println();
		pri(ret2);
	}

}
