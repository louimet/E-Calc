/*
 * Written by Laurent Ouimet for COPM 5541, calculator project
 * Winter 2016
 */
package com.teamE;
/**
 * Class for approximating common logarithm (base 10)
 * depends on NaturalLog
 */
public class Log10 {
	/**
	 * Approximate the natural logarithm
	 * @param number the number on which we approximate the logarithm
	 * @return the approximate logarithm
	 */

	public static double calculate(double number) {
		double exponent = 0;

		// Special cases (0 and 1) are handled first.
		if (number <= 0) {
			double badDouble = Double.NaN;
			exponent = badDouble;
		} else if (number == 1) {
			exponent = 0;
		} else {
			// If number is positive but smaller than 0.1, multiply by 10.
			for (; number > 0 && number < 0.1; exponent--) {
				number *= 10;
			}
			// If number is greater than 1, divide by 10.
			for (; number > 1; exponent++) {
				number /= 10;
			}
			// Feed remaining value to NaturalLog and "assemble" final result.
			exponent += (NaturalLog.calculate(number)
					/ NaturalLog.calculate(10));
		}
		return exponent;
	}
}
