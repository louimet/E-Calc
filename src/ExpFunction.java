/**
 * Written by: Yuanwen Qin,  40011473
 * General Description: This class achieves an approximation to the exponential function exp(x)
 * The algorithm for the approximation:
 * 			 n	
 * exp(x) = Sum ((x^k)/k!)
 * 			k=0
 * The precision of the output increases as the number of iteration n increases.
 * */

public class ExpFunction {
	
	private static int nIter = 10;//number of iteration, 10 by default. The precision of the output increases as nIter increases.
	
	/**
	 * This method takes a double type input x and outputs exp(x)
	 */
	static public double calculate(double val){
		double result = 1;
		double temp = 1;
		for (int i = 1; i <= nIter; i++){
			temp = temp * val / i;
			result += temp;
		}
		return result;	
	}
	
	/**
	 * If no input is given then outputs exp(1)
	 */
	static public double calculate(){
		return calculate(1);
	}
	
	/**
	 * Set the number of iteration to increase/decrease the precision of the output
	 * precondition: n >= 0
	 */
	static public void setNrIter(int n){
		if (n < 1){
			return;
		}
		nIter = n;
	}
}
