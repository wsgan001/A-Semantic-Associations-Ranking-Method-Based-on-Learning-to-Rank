package CollaborativeFiltering;

import Jama.Matrix;

public class JamaTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[][] a = {{1,2},{3,4}};
		double[][] b = {{1,0},{0,1}};
		Matrix ma = new Matrix(a);
		Matrix mb = new Matrix(b);
		Matrix mc = ma.times(mb);
		mc.print(3, 0);
	}

}
