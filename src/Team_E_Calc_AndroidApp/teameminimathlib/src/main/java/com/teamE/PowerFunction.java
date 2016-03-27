package com.teamE;

public class PowerFunction {
		
	// This is the method that approximates the function x^y. It takes two doubles, x and y and
	// returns x^y
	static public double calculate(double x, double y){
	/* Using the rules of natural logarithms we can see that:
	 * 
	 * x^y = e^(y*ln(x)).
	 *
	 * If the power to which x is raised is negative, the inverse must be taken as such:
	 * x^-y = (1/(x^y)).
	 * 
	 * If the power to which x is raised is equal to 0.0, the answer return 1.0.
	 * 
	 * If the power to which x is raised is larger than 0.0, the answer is x^y = e^(y*ln(x)).
	 * The e^y function is based on the implementation of calculate(double y) by Yuanwen Qin.
	*/
		double naturalLogx = NaturalLog.calculate(x);

		long integerPart = ((Double)y).longValue();
		double fractionalPart = y - integerPart;
		double temp = 1d;

		if (y<0)
		{
			for(int i = 0; i < -integerPart; i++)
				temp *= x;
			return 1.0/(temp*ExpFunction.calculate(-fractionalPart*naturalLogx));
		}
		else if (y==0)
		{
			return (1.0);
		}
		else 
		{
			for(int i = 0; i < integerPart; i++)
				temp *= x;
			return temp * ExpFunction.calculate(fractionalPart*naturalLogx);
		}
	}
}
