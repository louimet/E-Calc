/**
 *Written by Devang Patel
 *
 * This class computes the standard deviation of an array entry of numbers.
 *                                                                              n
 * The standard deviation is calculated according to the formula StDev = sqrt((Sum ((x_i - mean)^2))/n)
 *                                                                             i=1
 * where x_i are each of the entries and n is the number of entries.
 * 
 */

public class StandardDeviation1 {
	/**
	 * User inputs values into array x.
	 */
	public static double calculate(double[] x) {
		double sum = 0;
		double mean = 0;
		double valueMinusMean = 0;
		double sumOfValueMinusMeanSquared = 0;
		double stdev = 0;
		double arrayLength = x.length;

		/**
		 * Calculate mean of entries in array.
		 */
		for (int i = 0; i < arrayLength; i++) {
			sum = sum + x[i];
		}
		// System.out.println(sum);
		mean = sum / arrayLength;
		// System.out.println(mean);

		/**
		 * Calculate the sum of (entries - mean)^2
		 */
		for (int j = 0; j < arrayLength; j++) {
			valueMinusMean = (x[j] - mean) * (x[j] - mean);
			sumOfValueMinusMeanSquared = sumOfValueMinusMeanSquared + valueMinusMean;
		}
		// System.out.println(sumOfValueMinusMeanSquared);
		// System.out.println(1/arrayLength);
		/**
		 * Use calculateSqrt2 static method to calculate standard deviation.
		 */
		stdev = squareRoot2.calculate(sumOfValueMinusMeanSquared/arrayLength);

		return stdev;
	}
}


