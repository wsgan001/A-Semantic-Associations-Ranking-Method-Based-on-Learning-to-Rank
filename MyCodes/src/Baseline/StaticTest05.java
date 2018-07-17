package Baseline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import KMeansCluster.Process;

public class StaticTest05 {

	private static String[] tmp = new String[11];
	private static String str;
	private static String[] fnm = new String[1500];
	private static double[] sco = new double[1500];
	private static Integer cnt = 0;
	private static Vector<Integer>[] user = new Vector[31];
	private static Set<Integer>[] label = new HashSet[35];
	private static File tr, td, ta;
	private static FileWriter wtr, wtd, wta;
	private static DecimalFormat fm=new DecimalFormat("#0.00000000");
	private static final String StdFN = "./datas/2_TestLabel.txt";
	private static Process model = new Process();
	private static Integer[] kk = new Integer[31];
	private static Integer userAll = 0;
	private static Integer userAcc = 0;
	private static double[] wei = new double[20];
	private static Vector<Double> sta = new Vector<Double>();
	
	private static void judge(Integer id)
	{
		Integer sum = 0;
		double scoh = 0, scol = 0;
		userAll ++;
		try {
			InputStream ish = new FileInputStream(fnm[id] + "_xh.txt");
			InputStream isl = new FileInputStream(fnm[id] + "_xl.txt");
			Scanner sch = new Scanner(ish);
			Scanner scl = new Scanner(isl);
			while(sch.hasNextDouble() && scl.hasNextDouble())
			{
				double th = sch.nextDouble();
				double tl = scl.nextDouble();
				sum ++;
				scoh += th * wei[sum];
				scol += tl * wei[sum];
			}
			if((scoh - scol) * sco[id] > 0)userAcc ++;
			ish.close();isl.close();sch.close();scl.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void SolveStr()
	{
		Integer len = str.length();
		Integer ind = 0;
		tmp[0] = "";
		for(Integer i=0; i<len; ++i)
		{
			if(i>0 && str.charAt(i)=='\t' && str.charAt(i-1)!='\t')
			{ind ++; tmp[ind] = "";}
			if(str.charAt(i)!='\t')tmp[ind] += str.charAt(i);
		}
	}
	
	private static void UserRead()
	{
		Integer cnt = 0;
		for(Integer i=1; i<=30; ++i)
		{
			label[i] = new HashSet<Integer>();
			label[i].clear();
		}
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(StdFN))))
		{
			while ((str = bufferedReader.readLine()) != null)
			{
				cnt ++;
				String tmp[] = str.split(" ");
				for(Integer i=0; i<tmp.length; ++i)label[cnt].add(Integer.parseInt(tmp[i]));
			}
		} catch (IOException e) {
           e.printStackTrace();
       }
	}
	
	private static Vector<Integer> GetCluster(Integer k)
	{
		Entry<Integer[], Double> ret = model.cluster(k);
		Integer[] ret_label = ret.getKey();
		/*
		Integer[] cnt = new Integer[31];
		for(Integer i=0; i<=30; ++i)cnt[i] = 0;
		for(Integer i=0; i<30; ++i)
		{
			cnt[ ret_label[i] ] ++;
			System.out.print((i+1) + ":" + ret_label[i] + "  ");
			if(i%5==4)System.out.println();
		}
		for(Integer i=0; i<k; ++i)System.out.println(i + ":" + cnt[i]);
		*/
		Vector<Integer> fh = new Vector<Integer>();
		fh.clear();
		for(Integer i=0; i<30; ++i)fh.add(ret_label[i]);
		return fh;
	}
	
	public static void Generate(Integer k, Integer flag)
	{
		Vector<Integer> Kmeans = GetCluster(k);
		for(Integer cluid=1; cluid<=k; ++cluid)
		{
			userAll = userAcc = 0;
			for(Integer userid=1; userid<=30; ++userid)
			{
				if(Kmeans.elementAt(userid-1) != cluid-1)continue;
				for(Integer i=0; i<kk[userid]; ++i)
				{
					if((flag > 0) && (!label[userid].contains(i)))continue;
					judge(user[userid].elementAt(i));
				}
			}
			System.out.println("Cluster : " + cluid + "\tAcc/All = " + userAcc + "/" + userAll + "\t Accuracy = " + fm.format((double)userAcc / (double)userAll));
			sta.add((double)userAcc / (double)userAll);
		}
	}
	
	private static void Statistics()
	{
		Integer nu = sta.size();
		double ma = 0, mi = 1, sig = 0, miu = 0;
		for(double p : sta)
		{
			ma = Math.max(ma, p);
			mi = Math.min(mi, p);
			miu += p / (double)nu;
		}
		for(double p : sta)sig += (p-miu)*(p-miu);
		sig /= (double)nu;
		sig = Math.sqrt(sig);
		System.out.println("max = " + fm.format(ma) + "\tmin = " + fm.format(mi) + "\tmiu = " + fm.format(miu) + "\tsigma = " + fm.format(sig));
	}
	
	private static void test(Integer t1, Integer t2, Integer t3)
	{
		sta.clear();
		wei[0] = 0;
		if(t1 == 1) { wei[1] = 0.32; wei[2] = 0.12; wei[3] = 0.22; wei[4] = 0.22; wei[5] = 0.12; }
		else {wei[1] = wei[2] = wei[3] = wei[4] = wei[5] = 0.2;}
		
		try {
			model.loadData("./datas/UserVector.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Integer i=1; i<=30; ++i) kk[i] = 0;
		for(Integer i=1; i<=30; ++i)user[i] = new Vector<Integer>();
		cnt = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(new File("./datas/RankEval2017/result.tsv")));
           ) {
        		while( (str = br.readLine()) != null)
        		{
        			SolveStr();
        			tmp[1] = tmp[1].substring(5, tmp[1].length());
        			//System.out.println("1: " + tmp[0] + " 2: " + tmp[1] + " 3: " + tmp[2]);
        			Integer id = 0;id = Integer.parseInt(tmp[1]);
        			double score = 0;score = Double.parseDouble(tmp[2]);
        			if(Math.abs(score) < 0.1)continue;
        			cnt ++;kk[id] ++;
        			fnm[cnt] = "./datas/RankEval2017/data/" + tmp[0];
        			sco[cnt] = score;
        			user[id].add(cnt);
        		}
           } catch (IOException e) {
        	   System.out.println("!!! : " + str);
               e.printStackTrace();
           }
		UserRead();
		//for(Integer k=1; k<=30; ++k)Generate(k);
		Generate(t2,t3);Statistics();
		for(Double p : sta)System.out.print(fm.format(p)+", ");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test(1, 30, 2);
	}

}
