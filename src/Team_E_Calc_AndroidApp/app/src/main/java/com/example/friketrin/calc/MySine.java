package com.example.friketrin.calc;

public class MySine {
    /*
	 * Sources:
	 * SUPER GOOD CASE for CHEBYSCHEV
	 * http://stackoverflow.com/questions/345085/how-do-trigonometric-functions-work/394512#394512
	 * http://krisgarrett.net/papers/l2approx.pdf
	 * nice and simple: http://allenchou.net/2014/02/game-math-faster-sine-cosine-with-polynomial-curves/
	 * age-old-india, precise. http://www.math.umt.edu/tmme/vol11no3/Stroethoff_3.pdf
	 * good ' ol taylor http://www.math.utah.edu/~kitchen/1220F06/sine.html
	 * fixed point, fast, above my head: http://www.coranac.com/2009/07/sines/
	 * Heuristic: https://www.desmos.com/calculator/vuyrfflvx7
	 * Pade, beyond me: https://en.wikipedia.org/wiki/Pad%C3%A9_approximant
	 * another fast one: http://lab.polygonal.de/?p=205
	 *
	 * For PI: https://www.math.hmc.edu/cgi-bin/funfacts/main.cgi?Subject=00&Level=0&Keyword=pi%20formula
	 * concretely, I use https://www.math.hmc.edu/funfacts/ffiles/10004.5.shtml
	 */

    // for Allan Chou
    private static final double A0 = 1.0;
    private static final double A2 = 2.0 / MyPi.PI - 12.0 / (MyPi.PI_PI);
    private static final double A3 = 16.0 / (MyPi.PI_PI * MyPi.PI) - 4.0 / (MyPi.PI_PI);

    // use the symmetry of sine and calculate only 1 quadrant
    private static double chouHill(double x){
        return A0 + A2 * x*x + A3 * x*x*x; // Allan Chou method
    }

    private static double taylorHill(double x) {
        double x2 = x*x;
        double x3 = x*x2;
        double x5 = x3*x2;
        double x7 = x5*x2;
        double x9 = x7*x2;
        return 1 - (x3/6) + (x5/24) - (x7/5040) + (x9/362880);
    }


    private static final double cheby1 = + 9.99999999999999312880e-01;
    private static final double cheby2 = - 1.66666666666652434127e-01;
    private static final double cheby3 =  + 8.33333333324810981519e-03;
    private static final double cheby4 = - 1.98412698184898843225e-04;
    private static final double cheby5 = + 2.75573160083833319909e-06;
    private static final double cheby6 = - 2.50518516666250910087e-08;
    private static final double cheby7 = 1.60473922487682573331e-10;
    private static final double cheby8 = - 7.36644541924532787403e-13;

    // Pointwise Error Estimate: 3.62522610307114174520e-16

    private static double chebyHill(double x){
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
    { // http://allenchou.net/2014/02/game-math-faster-sine-cosine-with-polynomial-curves/
        if (isRadians){
            // surest branch first...
        }
        else {
            angle = (angle / 360.0) * MyPi.TWO_PI; // go with degrees or radians? this is in between
        }

        double offset = angle * MyPi.TWO_PI_INV;
        angle -= (int)offset * MyPi.TWO_PI;
        if(angle < 0.0) angle += MyPi.TWO_PI;

        // 4 pieces of hills
        if (angle < MyPi.HALF_PI)
            return chebyHill(MyPi.HALF_PI - angle);
        else if (angle < MyPi.PI)
            return chebyHill(angle - MyPi.HALF_PI);
        else if (angle < 3.0f * MyPi.HALF_PI)
            return -chebyHill(3.0f * MyPi.HALF_PI - angle);
        else
            return -chebyHill(angle - 3.0f * MyPi.HALF_PI);
    }

}
