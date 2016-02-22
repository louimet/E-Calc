package com.example.friketrin.calc;

/**
 * Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
 * Winter 2016
 * 
 * This classes calculates the approximate sine of a positive number.
 * By default input is assumed to be in radians, passing a second argument, boolean false, will calculate in degrees
 * It depends on a sister class Pi elaborated for the same project
 * 
 * An approximation via a Chebyshev 15th degree polynomial has been chosen for speed and accuracy 
 * The source of the approximation by Charles K. Garret from 2012 can be found in the following paper: 
 * http://krisgarrett.net/papers/l2approx.pdf
 */

public class Sine {
    
    private static final double cheby1 = + 9.99999999999999312880e-01;
    private static final double cheby2 = - 1.66666666666652434127e-01;
    private static final double cheby3 =  + 8.33333333324810981519e-03;
    private static final double cheby4 = - 1.98412698184898843225e-04;
    private static final double cheby5 = + 2.75573160083833319909e-06;
    private static final double cheby6 = - 2.50518516666250910087e-08;
    private static final double cheby7 = 1.60473922487682573331e-10;
    private static final double cheby8 = - 7.36644541924532787403e-13;
    // From the paper: Pointwise Error Estimate: 3.62522610307114174520e-16

    private static double cheby(double x){
        double result = 0;
        double x2 = x*x;
        result += cheby1*x;
        x *= x2;
        result += cheby2*x;
        x *= x2;
        result += cheby3*x;
        x *= x2;
        result += cheby7*x;
        x *= x2;
        result += cheby8*x;
        x *= x2;
        result += cheby4*x;
        x *= x2;
        result += cheby5*x;
        x *= x2;
        result += cheby6*x;

        return result;
    }

    public static double calculate (double angle){return calculate(angle, true);}

    public static double calculate(double angle, boolean isRadians)
    {
        if (isRadians){
            // surest branch first... do nothing
        }
        else {
            angle = (angle / 360.0) * Pi.TWO_PI; // go with degrees or radians? this is in between
        }

        Double offset = angle * Pi.TWO_PI_INV;
        offset = (double)(offset.intValue()); // truncate
        angle -= offset * Pi.TWO_PI;
        if(angle > Pi.PI) angle -= Pi.TWO_PI;
        else if(angle < -Pi.PI) angle += Pi.TWO_PI;

        // our aproximation is best from -pi/2 to +pi/2
        // use the symmetry of sine and calculate only 2 quadrants around 0
        if (angle < -Pi.HALF_PI)
            return cheby(-Pi.PI - angle);
        else if (angle < Pi.HALF_PI) 
            return cheby(angle);
        else // (Pi.HALF_PI < angle < Pi.PI)
            return cheby(Pi.PI - angle);
    }

}
