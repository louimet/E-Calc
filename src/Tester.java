/* This programs tests each of our functions
 * by feeding them a succession of numbers. */


public class Tester {
	public static void main(String[] args) {
		Double[] numbers = {0.0, 1.0, -1.0, 0.5, -0.5, Math.PI, -Math.PI, 100.0, -100.0, 123456.0, -123456.0};
		Double[] difference = new Double[numbers.length];
		Double[] relativeDifference = new Double[numbers.length];
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
			relativeDifference[i] = difference[i]/Math.abs(javas);
		}
		CalculateError(difference);
		CalculateRelativeError(relativeDifference);
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
			relativeDifference[i] = difference[i]/Math.abs(javas);
		}
		CalculateError(difference);
		CalculateRelativeError(relativeDifference);
		CalculateDuration(durationOurs, durationJavas);
		System.out.println();
		
		System.out.println("Testing Sine function :");
		for(int i = 0; i < numbers.length; i++) {
			start = System.nanoTime();
			double ours = Sine.calculate(numbers[i]);
			end = System.nanoTime();
			durationOurs[i] = end - start;
			start = System.nanoTime();
			double javas = Math.sin(numbers[i]);
			end = System.nanoTime();
			durationJavas[i] = end - start;
			System.out.println("Sine(" + numbers[i] + ") = " + ours + " (Java: " + javas +", difference of " + (ours - javas) + ")");
			difference[i] = Math.abs(ours - javas);
			relativeDifference[i] = difference[i]/Math.abs(javas);
		}
		CalculateError(difference);
		CalculateRelativeError(relativeDifference);
		CalculateDuration(durationOurs, durationJavas);
		System.out.println();
		
	}
	
	static public void CalculateError(Double[] difference) {
		double sum = 0;
		double averageError = 0;
		int validInputCount = 0;
		for(int i = 0; i < difference.length; i++) {
			if (!difference[i].isNaN() && !(difference[i]).isInfinite()){ 
				sum = sum + difference[i];
				validInputCount++;
			}
		}
		averageError = sum / validInputCount;
		System.out.println("Average error from Java's calculated value : " + averageError + ".");
		sum = 0;
	}
	
	static public void CalculateRelativeError(Double[] relativeDifference) {
		double sum = 0;
		double averageError = 0;
		int validInputCount = 0;
		for(int i = 0; i < relativeDifference.length; i++) {
			if (!relativeDifference[i].isNaN() && !(relativeDifference[i]).isInfinite()){ 
				sum = sum + relativeDifference[i];
				validInputCount++;
			}
		}
		averageError = sum / validInputCount;
		System.out.println("Average relative error from Java's calculated value : " + averageError + ".");
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
