/**
 * Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
 * Winter 2016
 * 
 * This classes calculates the approximate sine of a positive number.
 * By default input is assumed to be in radians, passing a second argument, boolean false, will calculate in degrees
 * It depends on a sister class Pi elaborated for the same project
 * 
 * This is a 13th order Taylor series approximation from the following site by Sarah Kitchen: 
 * http://www.math.utah.edu/~kitchen/1220F06/sine.html (there are a myriad sites on the topic but this one is nice)
 */

public class SinAlt1 {

    private static double taylor(double x) {
        double x2 = x*x;
        double x3 = x*x2;
        double x5 = x3*x2;
        double x7 = x5*x2;
        double x9 = x7*x2;
        double x11 = x9*x2;
        double x13 = x11*x2;
        return x - (x3/6) + (x5/24) - (x7/5040) + (x9/362880) - (x11/39916800) + (x13/6227020800l);
    }

    public static double calculate (double angle){return calculate(angle, true);}

    public static double calculate(double angle, boolean isRadians)
    {
        if (isRadians){
            // surest branch first... we do nothing here
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
