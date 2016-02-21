/**
 * Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
 * Winter 2016
 * 
 * This classes calculates the approximate sine of a positive number.
 * By default input is assumed to be in radians, passing a second argument, boolean false, will calculate in degrees
 * It depends on a sister class Pi elaborated for the same project
 * 
 * This is a fast but innacurate approximation from the following site by Allen Chou: 
 * nice and simple: http://allenchou.net/2014/02/game-math-faster-sine-cosine-with-polynomial-curves/
 */
public class SinAlt2 {
	// for Allan Chou's method
    private static final double A0 = 1.0;
    private static final double A2 = 2.0 / Pi.PI - 12.0 / (Pi.PI_PI);
    private static final double A3 = 16.0 / (Pi.PI_PI * Pi.PI) - 4.0 / (Pi.PI_PI);

    private static double chou(double x){
        return A0 + A2 * x*x + A3 * x*x*x; // Allan Chou method
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
        
        // chou's method actually gives cosine, sin(x+pi/2) = cos(x)
        angle += Pi.HALF_PI;
        Double offset = angle * Pi.TWO_PI_INV;
        offset = (double)(offset.intValue()); // truncate
        angle -= offset * Pi.TWO_PI;
        if(angle > Pi.PI) angle -= Pi.TWO_PI;
        else if(angle < -Pi.PI) angle += Pi.TWO_PI;

        // our aproximation is best from -pi/2 to +pi/2
        // use the symmetry of sine and calculate only 2 quadrants around 0
        if (angle < -Pi.HALF_PI)
            return chou(-Pi.PI - angle);
        else if (angle < Pi.HALF_PI) 
            return chou(angle);
        else // (Pi.HALF_PI < angle < Pi.PI)
            return chou(Pi.PI - angle);
    }
}
    