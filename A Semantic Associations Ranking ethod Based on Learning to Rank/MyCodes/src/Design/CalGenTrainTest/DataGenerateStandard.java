package Design.CalGenTrainTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.Vector;

import Design.DbHelper;

import java.util.HashSet;
import java.util.Scanner;

public class DataGenerateStandard {

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
	private static final String StdFN = "./datas/TestLabel.txt";
	
	private static void WriteTest(Integer id)
	{
		Integer sum = 0;
		try {
			InputStream ish = new FileInputStream(fnm[id] + "_xh.txt");
			InputStream isl = new FileInputStream(fnm[id] + "_xl.txt");
			Scanner sch = new Scanner(ish);
			Scanner scl = new Scanner(isl);
			String pos = "";
			String neg = "";
			while(sch.hasNextDouble() && scl.hasNextDouble())
			{
				double th = sch.nextDouble();
				double tl = scl.nextDouble();
				sum ++;
				pos += sum.toString() + ":" + fm.format(th) + " ";
				neg += sum.toString() + ":" + fm.format(tl) + " ";
				sum ++;
				pos += sum.toString() + ":" + fm.format(tl) + " ";
				neg += sum.toString() + ":" + fm.format(th) + " ";
			}
			pos += "\r\n";neg += "\r\n";
			if(Math.abs(sco[id]) < 0.1)
			{
				wtd.write("2 " + pos);
				wtd.write("2 " + neg);
			}
			else if(sco[id] > 0)
			{
				wtd.write("1 " + pos);
				wtd.write("0 " + neg);
			}else
			{
				wtd.write("0 " + pos);
				wtd.write("1 " + neg);
			}
			if(Math.abs(sco[id]) < 0.1)wta.write("2 2 ");else if(sco[id] > 0)wta.write("1 0 ");else wta.write("0 1 ");
			ish.close();isl.close();sch.close();scl.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void WriteTrain(Integer id)
	{
		Integer sum = 0;
		try {
			InputStream ish = new FileInputStream(fnm[id] + "_xh.txt");
			InputStream isl = new FileInputStream(fnm[id] + "_xl.txt");
			Scanner sch = new Scanner(ish);
			Scanner scl = new Scanner(isl);
			String pos = "";
			String neg = "";
			while(sch.hasNextDouble() && scl.hasNextDouble())
			{
				double th = sch.nextDouble();
				double tl = scl.nextDouble();
				sum ++;
				pos += sum.toString() + ":" + fm.format(th) + " ";
				neg += sum.toString() + ":" + fm.format(tl) + " ";
				sum ++;
				pos += sum.toString() + ":" + fm.format(tl) + " ";
				neg += sum.toString() + ":" + fm.format(th) + " ";
			}
			pos += "\r\n";neg += "\r\n";
			if(Math.abs(sco[id]) < 0.1)
			{
				wtr.write("2 " + pos);
				wtr.write("2 " + neg);
			}
			else if(sco[id] > 0)
			{
				wtr.write("1 " + pos);
				wtr.write("0 " + neg);
			}else
			{
				wtr.write("0 " + pos);
				wtr.write("1 " + neg);
			}
			ish.close();isl.close();sch.close();scl.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void UserGen(Integer userid)
	{
		String train_name = "./datas/SVM/Train/Train_" + userid.toString() + ".txt";
		String testdata_name = "./datas/SVM/Test/TestData_" + userid.toString() + ".txt";
		String testansw_name = "./datas/SVM/Test/TestAnsw_" + userid.toString() + ".txt";
		tr = new File(train_name);
		td = new File(testdata_name);
		ta = new File(testansw_name);
		try {
			wtr = new FileWriter(tr);
			wtd = new FileWriter(td);
			wta = new FileWriter(ta);
			for(Integer i=0; i<40; ++i)
			{
				if(label[userid].contains(i))
				{
					//test
					WriteTest(user[userid].elementAt(i));
				}else
				{
					//train
					WriteTrain(user[userid].elementAt(i));
				}
			}
			wtr.close();wtd.close();wta.close();
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
        			cnt ++;
        			fnm[cnt] = "./datas/RankEval2017/data/" + tmp[0];
        			sco[cnt] = score;
        			user[id].add(cnt);
        		}
           } catch (IOException e) {
        	   System.out.println("!!! : " + str);
               e.printStackTrace();
           }
		UserRead();
		//for(Integer i=1; i<=30; ++i)System.out.println(user[i].size());
		for(Integer i=1; i<=30; ++i)
		{
			UserGen(i);
			System.out.println("End " + i.toString());
		}
	}

}
