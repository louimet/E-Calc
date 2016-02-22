/* This programs tests each of our functions
 * by feeding them a succession of numbers. */


public class Tester {
	public static void main(String[] args) {
		double[] numbers = {0.0, 1.0, -1.0, 0.5, -0.5, Math.PI, -Math.PI, 100, -100, 123456, -123456};
		double[] difference = new double[numbers.length];
		double[] relative = new double[numbers.length];
		long[] durationOurs = new long[numbers.length];
		long[] durationJavas = new long[numbers.length];
		long start, end;
		
		System.out.println("Testing e^x function :");
		for(int i = 0; i < numbers.length; i++) {
			start = System.nanoTime();
			double ours = ExpFunction.calculate(numbers[i]);
			end = System.nanoTime();
			durationOurs[i] = end - start;
			start = System.nanoTime();
			double javas = Math.exp(numbers[i]);
			end = System.nanoTime();
			durationJavas[i] = end - start;
			System.out.println("e^" + numbers[i] + " = " + ours + " (Java: " + javas +", difference of " + (ours - javas) + ")");
			difference[i] = Math.abs(ours - javas);
			relative[i] = Math.abs((ours - javas) / javas);
		}
		CalculateError(relative);
		CalculateDuration(durationOurs, durationJavas);
		System.out.println();
		
		System.out.println("Testing log10 function :");
		for(int i = 0; i < numbers.length; i++) {
			start = System.nanoTime();
			double ours = Log10.calculate(numbers[i]);
			end = System.nanoTime();
			durationOurs[i] = end - start;
			start = System.nanoTime();
			double javas = Math.log10(numbers[i]);
			end = System.nanoTime();
			durationJavas[i] = end - start;
			System.out.println("Log(" + numbers[i] + ") = " + ours + " (Java: " + javas +", difference of " + (ours - javas) + ")");
			difference[i] = Math.abs(ours - javas);
		}
		CalculateError(difference);
		CalculateDuration(durationOurs, durationJavas);
		System.out.println();
		
		System.out.println("Testing square root (1) function :");
		for(int i = 0; i < numbers.length; i++) {
			start = System.nanoTime();
			double ours = squareRoot1.calculate(numbers[i]);
			end = System.nanoTime();
			durationOurs[i] = end - start;
			start = System.nanoTime();
			double javas = Math.sqrt(numbers[i]);
			end = System.nanoTime();
			durationJavas[i] = end - start;
			System.out.println("Square root of " + numbers[i] + " = " + ours + " (Java: " + javas +", difference of " + (ours - javas) + ")");
			difference[i] = Math.abs(ours - javas);
			relative[i] = Math.abs((ours - javas) / javas);
		}
		CalculateError(relative);
		CalculateDuration(durationOurs, durationJavas);
		System.out.println();
		
		System.out.println("Testing square root (2) function :");
		for(int i = 0; i < numbers.length; i++) {
			start = System.nanoTime();
			double ours = squareRoot2.calculate(numbers[i]);
			end = System.nanoTime();
			durationOurs[i] = end - start;
			start = System.nanoTime();
			double javas = Math.sqrt(numbers[i]);
			end = System.nanoTime();
			durationJavas[i] = end - start;
			System.out.println("Square root of " + numbers[i] + " = " + ours + " (Java: " + javas +", difference of " + (ours - javas) + ")");
			difference[i] = Math.abs(ours - javas);
			relative[i] = Math.abs((ours - javas) / javas);
		}
		CalculateError(relative);
		CalculateDuration(durationOurs, durationJavas);
		System.out.println();
		
/*		System.out.println("Testing square root (3) function :");
		for(int i = 0; i < numbers.length; i++) {
			start = System.nanoTime();
			double ours = squareRoot3.calculate(numbers[i]);
			end = System.nanoTime();
			durationOurs[i] = end - start;
			start = System.nanoTime();
			double javas = Math.sqrt(numbers[i]);
			end = System.nanoTime();
			durationJavas[i] = end - start;
			System.out.println("Square root of " + numbers[i] + " = " + ours + " (Java: " + javas +", difference of " + (ours - javas) + ")");
			difference[i] = Math.abs(ours - javas);
			relative[i] = Math.abs((ours - javas) / javas);
		}
		CalculateError(relative);
		CalculateDuration(durationOurs, durationJavas);
		System.out.println();
*/
		
		System.out.println("Testing 10^x function :");
		for(int i = 0; i < numbers.length; i++) {
			start = System.nanoTime();
			double ours = PowerOfTen.calculate(numbers[i]);
			end = System.nanoTime();
			durationOurs[i] = end - start;
			start = System.nanoTime();
			double javas = Math.pow(10.0, numbers[i]);
			end = System.nanoTime();
			durationJavas[i] = end - start;
			System.out.println("10^" + numbers[i] + " = " + ours + " (Java: " + javas +", difference of " + (ours - javas) + ")");
			difference[i] = Math.abs(ours - javas);
			relative[i] = Math.abs((ours - javas) / javas);
		}
		CalculateError(relative);
		CalculateDuration(durationOurs, durationJavas);
		System.out.println();
		
	}
	
	static public void CalculateError(double[] difference) {
		double sum = 0;
		double averageError = 0;
		for(int i = 0; i < difference.length; i++) {
			sum = sum + difference[i];
		}
		averageError = sum / difference.length;
		System.out.println("Average absolute error from Java's calculated value : " + averageError + ".");
		sum = 0;
	}
	
	static public void CalculateDuration(long[] ours, long[] javas) {
		long sum = 0;
		long averageOurs;
		long averageJavas;
		long difference;
		
		for(int i = 0; i < ours.length; i++) {
			sum = sum + ours[i];
		}
		averageOurs = (sum / ours.length) / 1000;
		for(int i = 0; i < javas.length; i++) {
			sum = sum + javas[i];
		}
		averageJavas = (sum / javas.length) / 1000;
		
		difference = averageOurs - averageJavas;
		System.out.println("Average time : " + averageOurs + " ms (ours), " + averageJavas + " ms (Java's).");
		System.out.println("Difference : " + difference + " milliseconds");
	}
}
