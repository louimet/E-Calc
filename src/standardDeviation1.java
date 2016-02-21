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

import java.util.Scanner;

public class standardDeviation1 {

	public static double calculateStandardDeviation1(double[] inputArray) {
		double sum = 0;
		double mean = 0;
		double valueMinusMean = 0;
		double sumOfValueMinusMeanSquared = 0;
		double stdev = 0;
		double arrayLength = inputArray.length;

		/**
		 * Calculate mean of entries in array.
		 */
		for (int i = 0; i < arrayLength; i++) {
			sum = sum + inputArray[i];
		}
		// System.out.println(sum);
		mean = sum / arrayLength;
		// System.out.println(mean);

		/**
		 * Calculate the sum of (entries - mean)^2
		 */
		for (int j = 0; j < arrayLength; j++) {
			valueMinusMean = (inputArray[j] - mean) * (inputArray[j] - mean);
			sumOfValueMinusMeanSquared = sumOfValueMinusMeanSquared + valueMinusMean;
		}
		// System.out.println(sumOfValueMinusMeanSquared);
		// System.out.println(1/arrayLength);
		/**
		 * Use calculateSqrt2 static method to calculate standard deviation.
		 */
		stdev = squareRoot2.calculateSqrt2(sumOfValueMinusMeanSquared/arrayLength);

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
		System.out.println(calculateStandardDeviation1(stdevEntries));

	}

}
