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
import java.util.Scanner;

public class standardDeviation3 {

	public static double calculateStandardDeviation3(double[] inputArray) {
		double entrySquared = 0;
		double sumOfSquares = 0;
		double sumOfEntries = 0;
		double stdev = 0;
		double arrayLength = inputArray.length;

		/**
		 * Calculate the sum of the squares of each entry in the array.
		 */
		for (int i = 0; i < inputArray.length; i++) {
			entrySquared = inputArray[i] * inputArray[i];
			sumOfSquares = sumOfSquares + entrySquared;
		}

		/**
		 * Calculate the sum of each entry in the array.
		 */
		for (int j = 0; j < inputArray.length; j++) {
			sumOfEntries = sumOfEntries + inputArray[j];
		}
		sumOfEntries = sumOfEntries * sumOfEntries;

		/**
		 * Use calculateSqrt2 static method to calculate standard deviation.
		 */
		stdev = squareRoot2.calculateSqrt2((1 / (arrayLength * (arrayLength - 1))) * ((arrayLength * sumOfSquares) - sumOfEntries));

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
		System.out.println(calculateStandardDeviation3(stdevEntries));

	}

}
