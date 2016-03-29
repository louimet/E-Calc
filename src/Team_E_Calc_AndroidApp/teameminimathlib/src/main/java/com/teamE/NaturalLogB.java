public class NaturalLogB{
	/** 
	 * This class calculates the natural logarithm of a number between 0 and 1
	 * using the Maclaurin series log(1-x) = -Sigma(x^n / n)
	*/
	static double calculate(double x) {
		double number = 1 - x;
		double result = 0;
		
		for(int i = 1; i < 99; i++) {
			result = result + (Math.pow(number, i) / i);
			//System.out.println(number + ", " + i + ", " + result);
		}
		result = 0 - result;
	
	return result;
	}		
}
