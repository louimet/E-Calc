/* Written by Laurent Ouimet
 *
 * The log10 method approximates the logarithm in base 10 of a given number.
 * 
 * At the first step, the difference between [10 to the approximated exponent] and [the original number]
 * is calculated.
 * 
 * If that difference is greater than 0, the approximated exponent is increased by 1.
 * 
 * If the difference is less than 0, the exponent is decreased by 0.1.
 * 
 * These steps are repeated until the difference is less than the stated precision.
 * 
 * At each successive step, the threshold that determines
 * whether the exponent should be incremented or decremented
 * is decreased by a factor of 10. The value by which the exponent
 * is incremented or decremented also decreases by a factor of 10 each time.
 * 
 * In other words, the exponent is incremented by 0.1 until 10^exponent > number.
 * It is then decreased by 0.01 until 10^exponent < number.
 * On the next pass, the increment is 0.01 and the decrement 0.001, and so on.
 */

public class Log10 {
	static public double calculate(double number) {
	// public static void main(String[] args) {
		// double number = 0.0;	// Remove in final version (number will be fed directly to method)
		if(number <= 0) {
			double badDouble = Double.NaN;
			return badDouble;
		} else if(number == 1) {
			return 0;
		} else {
			double approx = 0;
			double exponent = 0;
			double newIncrement = 0.1;
			double oldIncrement = 0;
			double precision = 0.000001;	// Eventually, should be determined by the order of magnitude of the number entered
			
			for(; (number - approx) > 0; exponent = exponent + 1) {
				approx = Math.pow(10, exponent);	// Replace by a call of Boulos's 10^x method
				//System.out.println(exponent);
			}
				
				//
			while(Math.abs(number - approx) > precision) {	
				for(; (number - approx) < oldIncrement; exponent = exponent - newIncrement) {
					approx = Math.pow(10, exponent);	// Replace by a call of Boulos's 10^x method
					//System.out.println(exponent);
					//System.out.println((number - approx) + "+" + oldIncrement + " " + newIncrement);
				}
				for(; (number - approx) > newIncrement; exponent = exponent + newIncrement) {
					approx = Math.pow(10, exponent);	// Replace by a call of Boulos's 10^x method
					//System.out.println(exponent);
					//System.out.println((number - approx) + "-" + newIncrement);
				}
				oldIncrement = newIncrement;
				newIncrement = newIncrement / 10;
			}				
			return exponent;
			//System.out.println(number + " equals 10 to the power of " + exponent + ".");	// Comment out in final version.
			//System.out.println("10^" + exponent + " = " + Math.pow(10, exponent));		// Comment out in final version.
		}
	}
}
