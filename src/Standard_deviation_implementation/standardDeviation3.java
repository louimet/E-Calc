/**
 * Written by Devang Patel
 * 
 * This class computes the standard deviation of an array entry of numbers.
 * The standard deviation is calculated using the sum of squares method according to 
 *                                             n                n
 * the formula StDev = sqrt((1/(n*(n-1)))*((n*Sum(x_i ^2)) - ((Sum x_i)^2)))
 *                                            i=1              i=1
 * where x_i are each of the entries and n is the number of entries.                                   
 */

public class StandardDeviation3 {

/**
	 * User inputs values into array x.
	 */
	public static double calculate(double[] x) {
		double entrySquared = 0;
		double sumOfSquares = 0;
		double sumOfEntries = 0;
		double stdev = 0;
		double arrayLength = x.length;

		/**
		 * Calculate the sum of the squares of each entry in the array.
		 */
		for (int i = 0; i < x.length; i++) {
			entrySquared = x[i] * x[i];
			sumOfSquares = sumOfSquares + entrySquared;
		}

		/**
		 * Calculate the sum of each entry in the array.
		 */
		for (int j = 0; j < x.length; j++) {
			sumOfEntries = sumOfEntries + x[j];
		}
		sumOfEntries = sumOfEntries * sumOfEntries;

		/**
		 * Use calculateSqrt2 static method to calculate standard deviation.
		 */
		stdev = squareRoot2.calculate((1 / (arrayLength * (arrayLength - 1))) * ((arrayLength * sumOfSquares) - sumOfEntries));

		return stdev;
	}
}
