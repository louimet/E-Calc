/*
 * Written by Boulos Salame for COMP 5541, calculator project
 * Winter 2016
 */

package com.teamE;

/**
 * Class that approximates the natural logarithm of a number via a Taylor series
 * expansion
 */
public class NaturalLog{

	static final double e = ExpFunction.calculate();

	/** The following method is an approximation to the natural logarithm
	 * function. It takes one argument; a double value whose natural
	 * logarithmic value needs to be found. The series is centered at one
	 * and is most precise in that vicinity
	 * @param x the number for which we approximate the natural logarithm
	 * @return and approximation of the natural logarithm of x
	*/
	public static double calculate(double x) {
		double fractionalPart = 0d;
		double intermediate;
		double intermediateSqr;
		double approximation;

		double temp = (x > 1)
				? x
				: 1d/x;

		double exponentWholePart = 0d;
		while (temp > 1) {
			temp /= e;
			exponentWholePart++;
		}

		/*
		 * Ln(x) = 2 [(x-1)/(x+1) + (1/3)*((x-1)/(x+1))^3 +
		 * (1/5)*((x-1)/(x+1))^5 + (1/7)*((x-1)/(x+1))^7...]
		 * Source: http://www.math.com/tables/expansion/log.htm
		 * A taylor series of 50th order is enough to give a relative
		 * error of 3.953*10^-9 for ln(10).
		 */
		intermediate = (temp - 1.0) / (temp + 1.0);
		intermediateSqr = intermediate * intermediate;
		for (int n = 1; n < 50; n += 2) {
			fractionalPart += intermediate / (n);
			intermediate *= intermediateSqr;
		}

		approximation = (2 * fractionalPart) + exponentWholePart;
		return((x > 1)
				? approximation
				: -approximation);
	}		
}