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
            return taylorHill(MyPi.HALF_PI - angle);
        else if (angle < MyPi.PI)
            return taylorHill(angle - MyPi.HALF_PI);
        else if (angle < 3.0f * MyPi.HALF_PI)
            return -taylorHill(3.0f * MyPi.HALF_PI - angle);
        else
            return -taylorHill(angle - 3.0f * MyPi.HALF_PI);
    }

}
