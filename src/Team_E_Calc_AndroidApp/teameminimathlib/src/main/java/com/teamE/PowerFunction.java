/*
 * Written by Federico O'Reilly for COMP 5541, calculator project
 * Winter 2016
 */

package com.teamE;

/**
 * Class that estimates x to the power of y, it relies on NaturalLogarithm
 * and ExponentialFunction in order to approximate the base raised to the
 * exponent ie. x^y = e^(y*ln(x)).
 */
public class PowerFunction {

	/**
	 * approximates base^exponent via a Taylor Series expansion of 50th order
	 * centered at 1
	 * @param base can be any real number
	 * @param exponent can be any real number
	 * @return ~ base ^ exponent
	 */
	static public double calculate(double base, double exponent){

		double naturalLogx = NaturalLog.calculate(base);

		long integerPart = ((Double)exponent).longValue();
		double fractionalPart = exponent - integerPart;
		double temp = 1d;
		double approximation;

		if (exponent<0) {
			for(int i = 0; i < -integerPart; i++) {
				temp *= base;
			}
			approximation
					= 1d
					/ (temp*ExpFunction.calculate(-fractionalPart*naturalLogx));
		} else if (exponent == 0) {
			approximation = 1.0;
		} else {
			for(int i = 0; i < integerPart; i++) {
				temp *= base;
			}
			approximation
					= temp * ExpFunction.calculate(fractionalPart*naturalLogx);
		}
		return approximation;
	}
}
