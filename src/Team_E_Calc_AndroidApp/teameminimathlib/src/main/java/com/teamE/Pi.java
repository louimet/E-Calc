/*
 * Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
 * Winter 2016
 */

package com.teamE;

/**
 * Needed for the sine function, the source for the approximation is:
 * https://en.wikipedia.org/wiki/Approximations_of_%CF%80 ,
 * 5419351.0/1725033.0 is a rational approximation of π with an absolute error
 * of 2.220446049250313e-14 vs python's math.PI (a better approximation).
 * For a smaller quotient, 355.0/113.0 gives an absolute error
 * of 2.667641894049666e-07 vs python's math.PI
 */
 
public class Pi{
    static public final double PI = 5419351.0/1725033.0;
    static final double HALF_PI = 0.5*PI;
    static final double TWO_PI = 2.0*PI;
    static final double TWO_PI_INV = 1.0/TWO_PI;
    static final double PI_PI = PI*PI;
}