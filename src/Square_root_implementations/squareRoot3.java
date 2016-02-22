/**
 * Written by Devang Patel
 * 
 * This class computes the approximation of the square root a positive number using the Bhaskara-Brouncker algorithm.
 * In this algorithm, sqrt(n) = x/y. The algorithm starts with some guess, x = y = 1, such that 1 is an approximation
 * to sqrt(n). Then according to the Bhaskara-Brouncker algorithm, a better approximation to sqrt(n) is 
 * (x+y*n)/(x+y), where x_i+1 = x_i + (y_i*n) and y_i+1 = x_i + y_i. Each approximation converges to the actual square 
 * root of n.
 * 
 */

public class SquareRoot3 {

public static double calculate(double x) {
		
		/**
		 * If the user enters a value less than 0, cannot compute square root. Display error.
		 */
		if (x < 0) {
			double badDouble = Double.NaN;
			return badDouble;
		}

		/**
		 * Since the Bhaskara-Brouncker algorithm starts by guessing that the square root of a value 
		 * is 1, the approximation for the square root of 0 will take an infinite number of iterations
		 * before the value is close to 0 because the algorithm used in this class ensures high 
		 * precision. Hence, it is best to handle the sqrt(0) separately. 
		 */
		else if (x == 0) {
			return 0;
		}

		/**
		 * Use Bhaskara-Brouncker algorithm to find the square root of value entered by user.
		 * The algorithm used is sqrt(n) = (x+y*n)/(x+y).
		 */
		else {
			double z = 1;
			double y = 1;
			double temp = 0;
			double approximation = 1;
			double previousApproximation = 0;

			while (approximation != previousApproximation) {
				previousApproximation = approximation;
				temp = z;
				z = z + (y * x);
				y = temp + y;
				approximation = z / y;
			}

			return approximation;

		}
	}

}
