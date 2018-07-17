package Design.CalGenTrainTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Vector;

public class ParaImportance {

	private static final String prefix = "./datas/RankEval2017/data/";
	private static String[] nm = new String[10];
	
	private static Vector<Double> tf = new Vector<Double>();
	private static Vector<Double> pff = new Vector<Double>();
	private static Vector<Double> pf = new Vector<Double>();
	private static Vector<Double> einf = new Vector<Double>();
	private static Vector<Double> rinf = new Vector<Double>();
	private static Vector<Double> spec = new Vector<Double>();
	private static Vector<Double> rhet = new Vector<Double>();
	private static Vector<Double> ehom = new Vector<Double>();
	private static Vector<Double> la = new Vector<Double>();
	private static Vector<Double> size = new Vector<Double>();
	private static Vector<Double> du = new Vector<Double>();
	
	private static void Read(String fm)
	{
		tf.clear();pff.clear();pf.clear();
		einf.clear();rinf.clear();spec.clear();la.clear();
		rhet.clear();ehom.clear();la.clear();size.clear();du.clear();
		try {
			InputStream io = new FileInputStream(fm);
			Scanner sc = new Scanner(io);
			Integer cnt = 0;
			while(sc.hasNextDouble())
			{
				Double p = sc.nextDouble();
				cnt ++;
				if(cnt == 1)spec.add(p);
				if(cnt == 2)rhet.add(p);
				if(cnt == 3)ehom.add(p);
				if(cnt == 4)size.add(p);
				if(cnt == 5)rinf.add(p);
				if(cnt == 6)einf.add(p);
				if(cnt == 7)du.add(p);
				if(cnt >= 8 && cnt <= 12)tf.add(p);
				if(cnt == 13)la.add(p);
				if(cnt >= 14 && cnt <=17)pff.add(p);
				if(cnt >= 18)pf.add(p);
			}
			sc.close();io.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void Write(String filenm, Integer label)
	{
		filenm += ".txt";
		File opf = new File(filenm);
		DecimalFormat fm=new DecimalFormat("#.00000000");
		try {
			FileWriter fw = new FileWriter(opf);
			if(label != 1) {for(Double p : spec)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 2) {for(Double p : rhet)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 3) {for(Double p : ehom)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 4) {for(Double p : size)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 5) {for(Double p : rinf)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 6) {for(Double p : einf)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 7) {for(Double p : du)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 8) {for(Double p : la)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 9) {for(Double p : tf)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 10) {for(Double p : pff)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			if(label != 11) {for(Double p : pf)fw.write(fm.format(p) + " ");fw.write("\r\n");}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void Solve(String fm, Integer label)
	{
		Read(fm + "_t.txt");
		Write(fm, label);
	}
	
	public static void main(Integer label) {
		// TODO Auto-generated method stub
		nm[1] = "Centr";
		nm[2] = "EHom";
		nm[3] = "EInf";
		nm[4] = "Freq";
		nm[5] = "RHet";
		nm[6] = "RInf";
		nm[7] = "Size";
		nm[8] = "Spec";
		for(Integer i=1; i<=8; ++i)
			for(Integer j=1; j<=30; ++j)
			{
				String fm = prefix + nm[i] + "_" + j.toString();
				Solve(fm + "_xh", label);
				Solve(fm + "_xl", label);
			}
	}

}
