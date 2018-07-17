package CollaborativeFiltering;

import CollaborativeFiltering.ColFil;

public class CFParametersTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer ioN = 30;
		double iotheta = 0.0;
		double ioalpha = 0.001;
		double iolambda = 0.2;
		while(ColFil.main(iotheta, ioalpha, iolambda, ioN) <= 92);
	}

}
