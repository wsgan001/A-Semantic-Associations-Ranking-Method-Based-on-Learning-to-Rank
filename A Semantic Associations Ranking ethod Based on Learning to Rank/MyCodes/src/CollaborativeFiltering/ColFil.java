package CollaborativeFiltering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import Jama.Matrix;

public class ColFil {

	private static final Integer UserNum = 30;
	private static final Integer SANum = 240;
	private static final Integer GradientCount = 20000;
	private static Integer N = 15;
	private static final String StdFN = "./datas/2_TestLabel.txt";
	//static double lamda = 0;
	private static double theta = 0;
	
	private static Matrix r = new Matrix(SANum, UserNum);
	private static Matrix TrainM = new Matrix(SANum, UserNum);
	//private static Matrix TestM = new Matrix(SANum, UserNum);
	private static Vector<Double> TestSco = new Vector<Double>();
	private static Vector<Integer> TestUser = new Vector<Integer>();
	private static Vector<Integer> TestSA = new Vector<Integer>();
	private static Matrix UserM = new Matrix(N, UserNum);
	private static Matrix SAM = new Matrix(N, SANum);
	
	private static Integer[] user = new Integer[31];
	private static Map<String, Integer> pre2id = new HashMap<String, Integer>();
	private static Set<Integer>[] label = new HashSet[35];
	private static Integer TestCnt = 0;
	private static Integer AccuCnt = 0;
	//UserM = N * UserNum
	//SAM = N * SAM
	
	private static Integer cs = 0;
	
	private static double CostFunction(double lambda)
	{
		double cost = 0, sum = 0;
		Matrix cha = SAM.transpose().times(UserM).minus(TrainM);
		for(Integer i=0; i<SANum; ++i)
			for(Integer j=0; j<UserNum; ++j)
				if(Math.abs(r.get(i, j) - 1.0) <= 0.1)
				{
					double tmp = cha.get(i, j);
					sum += tmp * tmp;
				}
		for(Integer i=0; i<N; ++i)
			for(Integer j=0; j<SANum; ++j)
			{
				double tmp = SAM.get(i, j);
				sum += tmp * tmp;
			}
		for(Integer i=0; i<N; ++i)
			for(Integer j=0; j<UserNum; ++j)
			{
				double tmp = UserM.get(i, j);
				sum += tmp * tmp;
			}
		cost /= 2.0;
		cost += sum * lambda / 2.0;
		return cost;
	}
	
	private static void GradientDescent(double alpha, double lambda)
	{	
		Matrix cha = SAM.transpose().times(UserM).minus(TrainM);
		for(Integer i=0; i<SANum; ++i)
			for(Integer j=0; j<UserNum; ++j)
				if(Math.abs(r.get(i, j) - 1.0) > 0.1)cha.set(i, j, 0.0);
		Matrix gdUserM = SAM.times(cha).plus(UserM.times(lambda));
		Matrix gdSAM = UserM.times(cha.transpose()).plus(SAM.times(lambda));
		UserM.minusEquals(gdUserM.times(alpha));
		SAM.minusEquals(gdSAM.times(alpha));
	}
	
	private static void ReadTest()
	{
		String str = "";
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
	
	private static String[] tmp = new String[11];
	
	private static void SolveStr(String str)
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
	
	private static Integer getSAid(String now)
	{
		String[] hh = now.split("_");
		Integer s1 = 0, s2 = 0;
		s1 = pre2id.get(hh[0]);
		s2 = Integer.parseInt(hh[1]);
		return (s1 - 1) * 30 + s2;
	}
	
	private static double ScoreProcess(double s)
	{
		//return s;
		if(Math.abs(s) < 0.1) return 0.0;
		if(s > 0)return 1.0;else return -1.0;
	}
	
	private static Boolean judge(double ret, double s)
	{
		if(Math.abs(s) < 0.1)
		{
			if(Math.abs(ret) < theta)return true;
			else return false;
		}
		if(s > 0)return ret >= theta;
		return ret <= -theta;
	}
	
	private static void DataGen()
	{
		for(Integer i=1; i<=30; ++i)user[i] = -1;
		String str = "";
		try (BufferedReader br = new BufferedReader(new FileReader(new File("./datas/RankEval2017/result.tsv")));
	       ){
		        while( (str = br.readLine()) != null)
		        {
		        	SolveStr(str);
		        	tmp[1] = tmp[1].substring(5, tmp[1].length());
		        	//System.out.println("1: " + tmp[0] + " 2: " + tmp[1] + " 3: " + tmp[2]);
		        	Integer id = 0;id = Integer.parseInt(tmp[1]);
		        	double score = 0;score = Double.parseDouble(tmp[2]);
		        	if(Math.abs(score) < 0.1)continue;
		        	score = ScoreProcess(score);
		        	Integer SAid = getSAid(tmp[0]);
		        	user[id] ++;
		        	if(label[id].contains(user[id]))
		        	{
		        		TestSco.add(score);
		        		TestUser.add(id);
		        		TestSA.add(SAid);
		        		TestCnt ++;
		        	}else
		        	{
		        		r.set(SAid-1, id-1, 1.0);
		        		TrainM.set(SAid-1, id-1, score);
		        	}
		        }
		    } catch (IOException e) {
		        System.out.println("!!! : " + str);
		        e.printStackTrace();
		    }
	}
	
	private static void Train(double alpha, double lambda)
	{
		UserM = Matrix.random(N, UserNum);
		SAM = Matrix.random(N, SANum);
		Integer cnt = GradientCount;
		while(cnt > 0)
		{
			cnt --;
			GradientDescent(alpha, lambda);
			//if(cnt % 100 == 0)System.out.println("After " + (GradientCount - cnt) + " steps : Cost is " + CostFunction(lambda));
		}
	}
	
	private static void Predict()
	{
		AccuCnt = 0;
		for(Integer i=0; i<TestCnt; ++i)
		{
			Integer userid = TestUser.elementAt(i) - 1;
			Integer said = TestSA.elementAt(i) - 1;
			double score = TestSco.elementAt(i);
			Matrix nowUser = UserM.getMatrix(0, N-1, userid, userid);
			Matrix nowSA = SAM.getMatrix(0, N-1, said, said);
			//System.out.print(nowUser.getRowDimension() + "*" + nowUser.getColumnDimension() + "   ");
			//System.out.println(nowSA.getRowDimension() + "*" + nowSA.getColumnDimension());
			double ret = nowUser.transpose().times(nowSA).get(0, 0);
			//System.out.print(ret + "   " + score);
			if(judge(ret, score))
			{
				AccuCnt ++;
				//System.out.println("  鈭�");
			}//else System.out.println("  脳");
		}
	}
	
	public static Integer main(double iotheta, double ioalpha, double iolambda, Integer ioN) {
		// TODO Auto-generated method stub
		AccuCnt = 0;TestCnt = 0;
		N = ioN;theta = iotheta;
		pre2id.clear();
		pre2id.put("Centr", 1);
		pre2id.put("EHom", 2);
		pre2id.put("EInf", 3);
		pre2id.put("Freq", 4);
		pre2id.put("RHet", 5);
		pre2id.put("RInf", 6);
		pre2id.put("Size", 7);
		pre2id.put("Spec", 8);
		ReadTest();DataGen();
		for(double i=iolambda; i<=3; i+=0.05)
		{
			Train(ioalpha, i);Predict();
			System.out.println("lambda : " + i + "\tAccuCnt/AllCnt : " + AccuCnt + "/" + TestCnt);
			break;
		}
		return AccuCnt;
		//System.out.println(AccuCnt + "/" + TestCnt);
	}

}
