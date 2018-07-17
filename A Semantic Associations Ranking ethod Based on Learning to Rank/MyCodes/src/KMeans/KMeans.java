package KMeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

class Point {
	private double[] pos;
	public Integer dimension;
	
	public Point(Integer size)
	{
		pos = new double[size];
		this.dimension = size;
	}
	public Point(double[] p)
	{
		this.pos = p;
		this.dimension = pos.length;
	}
	
	Integer getDimension()
	{
		return this.dimension;
	}
	
	double[] getPosition()
	{
		return pos;
	}
	
	public static double euclideanDistance(Point p1, Point p2)
	{
		if(p1.dimension != p2.dimension) return -1.0;
		double[] tmp = new double[p1.dimension];
		Integer dm = p1.dimension;
		for(Integer i=0; i<dm; ++i) tmp[i] = p1.pos[i] - p2.pos[i];
		double sum = 0.0;
		for(Integer i=0; i<dm; ++i) sum += Math.pow(tmp[i], 2.0);
		return Math.sqrt(sum);
	}
	
	public static double squareDistance(Point p1, Point p2)
	{
		if(p1.dimension != p2.dimension) return -1.0;
		double[] tmp = new double[p1.dimension];
		Integer dm = p1.dimension;
		for(Integer i=0; i<dm; ++i) tmp[i] = p1.pos[i] - p2.pos[i];
		double sum = 0.0;
		for(Integer i=0; i<dm; ++i) sum += Math.pow(tmp[i], 2.0);
		return sum;
	}
	
	public Integer compareTo(Object o)
	{
		Point p = (Point)o;
		if(this.dimension != p.dimension) return -100;
		for(Integer i=0; i<this.dimension; i++)
		{
			if(this.pos[i] < p.pos[i])return -1;
			if(this.pos[i] > p.pos[i])return 1;
		}
		return 0;
	}
	
	public boolean equals(Object o)
	{
		Point p = (Point)o;
		if(this.dimension != p.dimension) return false;
		for(Integer i=0; i<this.dimension; i++) if(this.pos[i] != p.pos[i]) return false;
		return true;
	}
}

class CluCen {
	public ArrayList<Integer> clusterPoints;
	private double[] sumOfPoints;
	private Integer dimension;
	private Point label;
	
	CluCen(Point p)
	{
		this.clusterPoints = new ArrayList<Integer>();
		this.sumOfPoints = new double[p.dimension];
		this.dimension = p.dimension;
		this.label = p;
	}
	
	Point getNowCen()
	{
		return label;
	}
	
	void addPoint(Integer index, Point p)
	{
		clusterPoints.add(index);
		double[] po = p.getPosition();
		for(Integer i=0; i<this.dimension; ++i) sumOfPoints[i] += po[i];
	}
	
	void getNewCenter()
	{
		double[] ret = new double[this.dimension];
		for(Integer i=0; i<this.dimension; ++i)ret[i] = sumOfPoints[i] / (double)this.clusterPoints.size();
		label = new Point(ret);
	}
	
	double evaluate(ArrayList<Point> poi)
	{
		double ret = 0.0;
		for(Integer idx : clusterPoints) ret += Point.squareDistance(poi.get(idx), label);
		return ret;
	}
	
	ArrayList<Integer> ContainedPoints()
	{
		return new ArrayList<Integer>(this.clusterPoints);
	}
}

public class KMeans {

	private static ArrayList<CluCen> Cent = new ArrayList<CluCen>();
	private static HashSet<Point> preCen = new HashSet<Point>();
	private static ArrayList<Point> Poi = new ArrayList<Point>();
	private static Integer PointCnt = 0;
	private static Integer PointDM = 0;
	private static final Integer MaxIte = 500;
	private static final Integer MaxCmp = 2000;
	
	public static void loadData(String fn) throws IOException
	{
		BufferedReader io = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fn))));
		String rd;
		while((rd = io.readLine()) != null)
		{
			PointCnt ++;
			String[] fs = rd.split(" +");
			double[] tmp = new double[fs.length];
			PointDM = fs.length;
			Integer i = 0;
			for(String s : fs)tmp[i++] = Double.valueOf(s);
			Poi.add(new Point(tmp));
		}
		io.close();
	}
	
	private static void InitialCen(Integer k)
	{
		Cent.clear();
		Random rand = new Random();
		HashSet<Integer> st = new HashSet<Integer>();
		st.clear();
		while(st.size() < k) st.add(rand.nextInt(PointCnt));
		for(Integer index : st) Cent.add(new CluCen(Poi.get(index)));
		//System.out.println(Cent.size());
	}

	public static ArrayList<CluCen> Cluster(Integer k)
	{
		Integer IteCnt = 0;
		while(IteCnt < MaxIte)
		{
			IteCnt ++;
			/*
			System.out.println(IteCnt);
			for(Integer i=0; i<k; ++i)
			{
				System.out.print("Cluster : " + i + "   ");
				for(Integer s : Cent.get(i).clusterPoints)System.out.print(" " + s);
				System.out.println();
			}
			System.out.println();
			*/
			preCen.clear();
			//System.out.println(k + "   " + Cent.size());
			for(Integer i=0; i<k; ++i)preCen.add(Cent.get(i).getNowCen());
			Cent.clear();
			for(Point clu : preCen)Cent.add(new CluCen(clu));
			for(Integer i=0; i<PointCnt; ++i)
			{
				Integer id = -1;
				double mi = 10000000000.0;
				Point now = Poi.get(i);
				for(Integer j=0; j<k; ++j)
				{
					double tmp = Point.euclideanDistance(now, Cent.get(j).getNowCen());
					if((id < 0) || (mi > tmp)) {id = j; mi = tmp;}
				}
				Cent.get(id).addPoint(i, now);
			}
			for(Integer j=0; j<k; ++j)Cent.get(j).getNewCenter();
			Boolean flag = true;
			for(Integer i=0; i<k && flag; ++i)if(!preCen.contains(Cent.get(i).getNowCen()))flag = false;
			if(flag) break;
		}
		return Cent;
	}
	
	private static double eva(ArrayList<CluCen> now)
	{
		double sum = 0;
		for(Integer i=0; i<now.size(); ++i) sum += now.get(i).evaluate(Poi);
		return sum;
	}
	
	public static Integer[] Solve(Integer k)
	{
		Integer CmpCnt = 0;
		ArrayList<CluCen> res = new ArrayList<CluCen>();
		double mi = 100000000000.0;
		while(CmpCnt < MaxCmp)
		{
			CmpCnt ++;
			InitialCen(k);
			ArrayList<CluCen> tmp = Cluster(k);
			double now = eva(tmp);
			if(now < mi) {mi = now; res = tmp;}
		}
		Integer[] ans = new Integer[PointCnt];
		for(Integer id=0; id<k; ++id)
		{
			CluCen now = res.get(id);
			for(Integer s : now.clusterPoints) ans[s] = id;
		}
		return ans;
	}
}
