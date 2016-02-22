/**
 * Written by Devang Patel
 * 
 * This classes calculates the approximate square root of a positive number.
 * 
 * The algorithm first computes the largest possible integer value n such that n < the square root of the number entered by the user.
 * Next, the algorithm adds 0.000000000001 to n iteratively until the value is approximately equal to the square root value. 
 * A higher precision can be achieved by incrementing the value of n with a value less than 0.000000000001.
 * 
 */
public class SquareRoot1 {
	/**
	 * x is the user-input.
	 */
	public static double calculate(double x) {

		/**
		 * If the user enters a value less than 0, cannot compute square root. Display error.
		 */
		if (x < 0) {
			double badDouble = Double.NaN;
			return badDouble;
		}

		/**
		 * Find the largest integer value n that is less than the square root of the value entered by the user.
		 */
		else {
			double approximation = 0;
			for (double i = 0; i >= 0; i++) {
				if ((i * i) > x) {
					break;
				}
				approximation = i;
			}
			
			/**
			 * Add 0.000000000001 iteratively to the largest integer value n calculated in the previous for loop 
			 * until n is approximately equal to the square root of the value entered by the user (i.e. 
			 * approximation^2 <= value entered by user).
			 */

			for (double j = 0.000000000001; j >= 0.000000000001;) {
				approximation = approximation + j;
				if ((approximation * approximation) > x) {
					approximation = approximation - j;
					break;
				}
				j = j + 0.000000000001;
			}

			return approximation;
		}
	}


