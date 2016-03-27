package com.teamE;

import java.math.BigInteger;

/**
 * Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
 * Winter 2016
 * 
 * This classes calculates the approximate sine of a positive number.
 * By default input is assumed to be in radians, passing a second argument, boolean false, will calculate in degrees
 * It depends on a sister class Pi elaborated for the same project
 *
 * This is a 19th order Taylor series approximation from the following site by Sarah Kitchen:
 * http://www.math.utah.edu/~kitchen/1220F06/sine.html (there are a myriad sites on the topic but this one is nice)
 */

public class Sine {

    private static final Long[] fac = {1L,6L,120L,5040L,362880L,39916800L,6227020800L,
           1307674368000L,355687428096000L, 121645100408832000L};

    private static double taylor(double x) {
        double x2 = x*x;
        double approximation = 0;
        int signum = 1; // alternate sign every term
        for (Long aFac : fac) {
            approximation += (signum * x / aFac);
            x *= x2;
            signum *= -1;
        }
        return approximation;
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
            return taylor(-Pi.PI - angle);
        else if (angle < Pi.HALF_PI) 
            return taylor(angle);
        else // (Pi.HALF_PI < angle < Pi.PI)
            return taylor(Pi.PI - angle);
    }

}
