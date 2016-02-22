
/**
 *Written by Devang Patel
 *
 * This class computes the standard deviation of an array entry of numbers.
 * The standard deviation is calculated using the Welford's algorithm which uses the
 * formula's m_n = m_n-1 + (x_n - m_n-1)/n and s_n = s_n-1 + (x_n - m_n-1)*(x_n - m_n), 
 * where m_1 is set to the value of the first entry in the array and n is the number of entries. This method only passes
 * through the array of entries once.
 * 
 */

public class StandardDeviation2 {

	/**
	 * User inputs values into array x.
	 */
	public static double calculate(double[] x) {
		double arrayLength = x.length;
		double m = x[0];
		double previous_m = 0;
		double s = 0;
		double previous_s = 0;
		double stdev = 0;

		/**
		 * Iterate through array of entries to calculate m and s.
		 */
		for (int i = 0; i < arrayLength; i++) {
			previous_m = m;
			previous_s = s;
			m = previous_m + ((x[i] - previous_m) / arrayLength);
			s = previous_s + ((x[i] - previous_m) * (x[i] - m));
		}

		/**
		 * Use calculateSqrt2 static method to calculate standard deviation.
		 */
		stdev = squareRoot2.calculate(s / (arrayLength - 1));
		return stdev;
	}
}
