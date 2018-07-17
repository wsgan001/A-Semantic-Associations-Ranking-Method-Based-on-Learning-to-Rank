package Design.UserClustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Scanner;

public class UserVectorGen {

	private static String[] st = new String[11];
	private static double[][] sco = new double[31][101];
	private static final String parafileprefix = "./datas/RankEval2017/data/";
	private static final double eps = 1e-7;
	private static Integer cou = 0;
	
	private static void SolveStr(String str)
	{
		Integer len = str.length();
		Integer ind = 0;
		st[0] = "";
		for(Integer i=0; i<len; ++i)
		{
			if(i>0 && str.charAt(i)=='\t' && str.charAt(i-1)!='\t'){ind ++; st[ind] = "";}
			if(str.charAt(i)!='\t')st[ind] += str.charAt(i);
		}
	}
	
	private static void GenerateVector(String fn, Integer userid, double score)
	{
		fn = parafileprefix + fn;
		try {
			InputStream ish = new FileInputStream(fn + "_xh.txt");
			InputStream isl = new FileInputStream(fn + "_xl.txt");
			Scanner sch = new Scanner(ish);
			Scanner scl = new Scanner(isl);
			Integer cnt = 0;
			while(sch.hasNextDouble() && scl.hasNextDouble())
			{
				double th = sch.nextDouble();
				double tl = scl.nextDouble();
				cnt ++;cou = Math.max(cou, cnt);
				if(Math.abs(th - tl) < eps)continue;
				if(th > tl)sco[userid][cnt] += score;else sco[userid][cnt] -= score;
			}
			sch.close();scl.close();ish.close();isl.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void pri(String filenm)
	{
		File opf = new File(filenm);
		DecimalFormat fm=new DecimalFormat("#0.00000000");
		try {
			FileWriter fw = new FileWriter(opf);
			for(Integer i=1; i<=30; ++i)
			{
				for(Integer j=1; j<=cou; ++j)fw.write(fm.format(sco[i][j]) + " ");
				fw.write("\r\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "";
		for(Integer i=1; i<=30; ++i)
			for(Integer j=1; j<=100; ++j)
				sco[i][j] = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(new File("./datas/RankEval2017/result.tsv"))))
		{
			while( (str = br.readLine()) != null)
    		{
    			SolveStr(str);
    			st[1] = st[1].substring(5, st[1].length());
    			System.out.println("1: " + st[0] + " 2: " + st[1] + " 3: " + st[2]);
    			Integer id = 0;id = Integer.parseInt(st[1]);
    			double score = 0;score = Double.parseDouble(st[2]);
    			GenerateVector(st[0], id, score);
    		}
          } catch (IOException e) {
        	   System.out.println("!!! : " + str);
               e.printStackTrace();
          }
		pri("./datas/UserVector.txt");
	}

}
