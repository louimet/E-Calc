public class Log10 {
	public static void main(String[] args) {
		double number = 123456789.0;
		double approx = 0;
		double exponent = 0;
		double newIncrement = 0.1;
		double oldIncrement = 0;
		double precision = 0.000001;
		
			for(; (number - approx) > 0; exponent = exponent + 1) {
				approx = Math.pow(10, exponent);
				//System.out.println(exponent);
			}
			
			//
		while(Math.abs(number - approx) > precision) {	
			for(; (number - approx) < oldIncrement; exponent = exponent - newIncrement) {
				approx = Math.pow(10, exponent);
				//System.out.println(exponent);
				//System.out.println((number - approx) + "+" + oldIncrement + " " + newIncrement);
			}
			for(; (number - approx) > newIncrement; exponent = exponent + newIncrement) {
				approx = Math.pow(10, exponent);
				//System.out.println(exponent);
				//System.out.println((number - approx) + "-" + newIncrement);
			}
			oldIncrement = newIncrement;
			newIncrement = newIncrement / 10;
		}				
		
		System.out.println(number + " equals 10 to the power of " + exponent + ".");
		System.out.println("10^" + exponent + " = " + Math.pow(10, exponent));
	}
}