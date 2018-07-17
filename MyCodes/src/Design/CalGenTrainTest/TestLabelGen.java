package Design.CalGenTrainTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TestLabelGen {

	private static final String StdFN = "./datas/2_TestLabel.txt";
	
	private static File fl;
	private static FileWriter fw;
	private static Set<Integer> lb = new HashSet<Integer>();
	
	private static String str = "";
	private static String[] tmp = new String[11];
	private static Integer[] kk = new Integer[31];
	
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for(Integer i=1; i<=30; ++i) kk[i] = 0;
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
		        			kk[id] ++;
		        		}
		           } catch (IOException e) {
		        	   System.out.println("!!! : " + str);
		               e.printStackTrace();
		           }
		
		fl = new File(StdFN);
		try {
			fw = new FileWriter(fl);
			for(Integer i=1; i<=30; ++i)
			{
				lb.clear();
				Integer tmpcnt = 5;
				while(tmpcnt > 0)
				{
					tmpcnt --;
					Integer ss = (int)(Math.random() * kk[i]);
					//System.out.println(ss);
					while(lb.contains(ss)){ss = (int)(Math.random() * kk[i]);}
					//System.out.println(ss);
					lb.add(ss);
					fw.write(ss.toString());if(tmpcnt > 0)fw.write(" ");else fw.write("\r\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
