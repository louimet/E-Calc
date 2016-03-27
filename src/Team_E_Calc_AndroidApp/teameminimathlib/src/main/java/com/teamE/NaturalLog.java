package com.teamE;

public class NaturalLog{

	static final double e = ExpFunction.calculate();

	/** The following method is an approximation to the natural logarithm function. It takes one 
	* argument; a double value whose natural logarithmic value needs to be found. The series is 
	* centered at one and is most precise in that vicinity
	*/
	static double calculate(double x)
	{
		double temp = (x > 1)
				? x
				: 1d/x;

		double exponentWholePart = 0d;
		while (temp > 1){
			temp /= e;
			exponentWholePart++;
		}

		// The following are initializations for the constant as well as all the intermediate values that
		// will be used in this method.
		double constant = (temp-1.0)/(temp+1.0);
		double constantSqr = constant * constant;
		temp = 0.0;

		/* A for loop that does the approximation. The following equation is what is being summed up by the for loop:
		 *
		 * Ln(x) = 2[(x-1)/(x+1) + (1/3)*((x-1)/(x+1))^3 + (1/5)*((x-1)/(x+1))^5 + (1/7)*((x-1)/(x+1))^7...]
		 * Source: http://www.math.com/tables/expansion/log.htm
		 * A taylor series of 50th order is enough to give a relative error of 3.953*10^-9 % based on the ln(10) example.
		 *
		 */
		for (int n = 1; n < 50; n += 2)
		{
			temp += constant/(n);
			constant *= constantSqr;
		}
		//Finally, the result of all the intermediate steps is multiplied by 2.0 to get the final result.
		double result = (2 * temp) + exponentWholePart; //
		result = (x > 1)
				? result
				: 1d / result;
		return result;
	}		
}