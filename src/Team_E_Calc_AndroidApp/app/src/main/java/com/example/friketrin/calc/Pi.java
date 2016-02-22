package com.example.friketrin.calc;

/**
 * Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
 * Winter 2016
 * 
 * We need this for the sine function, the source for the approximation is:
 * https://en.wikipedia.org/wiki/Approximations_of_%CF%80
 */
 
public class Pi{
    //355.0/113.0 gives an absolute error of 2.667641894049666e-07 vs python's math.PI
    static final double PI = 5419351.0/1725033.0; // an absolute error of 2.220446049250313e-14 vs python's math.PI
    static final double HALF_PI = 0.5*PI;
    static final double TWO_PI = 2.0*PI;
    static final double TWO_PI_INV = 1.0/TWO_PI;
    static final double PI_PI = PI*PI;
}