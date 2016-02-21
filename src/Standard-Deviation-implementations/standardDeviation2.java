
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

import java.util.Scanner;

public class standardDeviation2 {

	public static double calculateStandardDeviation2(double[] inputArray) {
		double arrayLength = inputArray.length;
		double m = inputArray[0];
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
			m = previous_m + ((inputArray[i] - previous_m) / arrayLength);
			s = previous_s + ((inputArray[i] - previous_m) * (inputArray[i] - m));
		}

		/**
		 * Use calculateSqrt2 static method to calculate standard deviation.
		 */
		stdev = squareRoot2.calculateSqrt2(s / (arrayLength - 1));
		return stdev;
	}

	public static void main(String[] args) {
		System.out.println("Enter the total number of entries.");
		Scanner keyboard = new Scanner(System.in);
		int numberOfEntries = keyboard.nextInt();
		double[] stdevEntries = new double[numberOfEntries];
		System.out.println("Input the entries.");
		for (int k = 0; k < stdevEntries.length; k++) {
			stdevEntries[k] = keyboard.nextDouble();
		}
		System.out.println(calculateStandardDeviation2(stdevEntries));

	}

}
