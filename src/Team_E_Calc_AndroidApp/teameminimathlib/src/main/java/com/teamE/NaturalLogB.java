package com.teamE;

public class NaturalLogB{
	/** 
	 * This class calculates the natural logarithm of a number between 0 and 1
	 * using the Maclaurin series log(1-x) = -Sigma(x^n / n)
	*/
	static double calculate(double x) {
		double result = 0;
		
		if(x <= 0) {
			double badDouble = Double.NaN;
			return badDouble;
		} else if(x == 1) {
			return 0;
		} else if(x > 0 && x < 1) {
			double number = 1 - x;			
			for(int i = 1; i < 99; i++) {
				result = result + (Math.pow(number, i) / i);
			}
			result = 0 - result;
			return result;
		} else {
			double temp = 1 / x;
			result = 0 - (NaturalLog.calculate(temp));
			return result;
		}
	}
}
