package com.teamE;

/**
 * Written by Devang Patel
 * Revised April 9, 2016
 *
 * This class computes the approximation of the square root of a positive number n
 * using Newton's iterations where x=sqrt(n). The algorithm starts with some guess,
 * x = 1, and computes a sequence of guesses that converge to the square root of n
 * with each iteration. The algorithm used is x_k+1 = 0.5*(x_k + (n/x_k)).
 */

public class SquareRoot {

    /**
     * x is the user-input.
     */
    public static double calculate(double x) {

        /**
         * Display error if the user enters a value less than 0.
         */
        if (x < 0) {
            double badDouble = Double.NaN;
            return badDouble;
        }

        /**
         * Since the Newton's iteration starts by guessing that the square root
         * of a value is 1, the approximation for the square root of 0 will take
         * an infinite number of iterations before the value is close to 0.
         * Hence, the sqrt(0) is handled separately.
         */
        else if (x == 0) {
            return 0;
        }

        /**
         * Use Newton's iterations to find the square root of value entered by
         * user. The algorithm used is x_k+1 = 0.5*(x_k + (n/x_k)).
         */
        else {
            double approximation = 1;
            double previousApproximation = 1;
            double nextApproximation = 0;
            while (nextApproximation != previousApproximation) {
                previousApproximation = nextApproximation;
                nextApproximation = 0.5 * (approximation + (x / approximation));
                approximation = nextApproximation;
            }
            return approximation;
        }
    }
}
