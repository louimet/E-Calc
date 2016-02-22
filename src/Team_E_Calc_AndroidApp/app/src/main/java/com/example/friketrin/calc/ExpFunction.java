package com.example.friketrin.calc;

/**
 * Written by: Yuanwen Qin,  40011473
 * General Description: This class achieves an approximation to the exponential function exp(x)
 * The algorithm for the approximation follows the Taylor series:
 * 			 n	
 * exp(x) = Sum ((x^k)/k!)
 * 			k=0
 * The precision of the output can be set according to the number of digits after the decimal point
 * */

public class ExpFunction {
	
	private static double precision = 1e-15;//The precision of the output; 10^-15 by default
	
	/**
	 * If no input is given then outputs exp(1)
	 */
	static public double calculate(){
		return calculate(1);
	}
	
	/**
	 * This method takes a double type input x and outputs exp(x)
	 * Sources: https://en.wikipedia.org/wiki/Exponential_function
	 */
	static public double calculate(double x){
		double result = 1;
		double xAbs; //absolute value of x;
		double temp = 1; // (x^k)/k!
		final int MAX_NUM_ITERATION = 1995; //the maximum number of iterations
		int k = 1;
		
		if (x >= 0)
			xAbs = x;
		else
			xAbs = -x;
		
		while (temp > precision && k < MAX_NUM_ITERATION){
			temp = temp * xAbs / k;
			result += temp;
			k++;
		}
				
		if (x < 0)
			result = 1/result;
		
		return result;
	}

	/**
	 * Set the precision of the output according to the number of digits after the decimal point
	 * @param: numDigits, number of digits after the decimal point
	 * pre: numDigits >= 1
	 */
	static public void setPrecision(int numDigits){
		if (numDigits < 1){
			return;
		}
		double precisionTemp = 1;
		for (int i = 1; i<= numDigits; i++){
			precisionTemp /= 10;
		}
		precision = precisionTemp;
	}
}
