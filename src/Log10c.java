
public class Log10c {
	static public double calculate(double number) {
		double exponent;
		
		if(number <= 0) {
			double badDouble = Double.NaN;
			return badDouble;
		} else if(number == 1) {
			return 0;
		} else {
			double temp = number;
			double floor = 0;
			for(;temp > 10; floor++) {
				temp = temp / 10;
			}
			temp = number / PowerOfTen.calculate(floor);
			exponent = floor + (PowerOfTen.naturalLog(temp) / PowerOfTen.naturalLog(10));
			
			System.out.println(number + " equals 10 to the power of " + exponent + ".");	// Comment out in final version.
			System.out.println("10^" + exponent + " = " + PowerOfTen.calculate(exponent));		// Comment out in final version.
			return exponent;
		}
	}
}
