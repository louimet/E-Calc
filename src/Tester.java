/* This programs tests each of our functions
 * by feeding them a succession of numbers. */


public class Tester {
	public static void main(String[] args) {
		double[] numbers = {0.0, 1.0, -1.0, 0.5, -0.5, 1234567890, Math.PI};
		double[] difference = new double[numbers.length];
		
		System.out.println("Testing e^x function :");
		for(int i = 0; i < numbers.length; i++) {
			double ours = ExpFunction.calculate(numbers[i]);
			double javas = Math.exp(numbers[i]);
			System.out.println("e^" + numbers[i] + " = " + ours + " (Java: " + javas +")");
			difference[i] = Math.abs(ours - javas);
		}
		CalculateError(difference);
		System.out.println();
		
		System.out.println("Testing log10 function :");
		for(int i = 0; i < numbers.length; i++) {
			double ours = Log10.calculate(numbers[i]);
			double javas = Math.log10(numbers[i]);
			System.out.println("Log(" + numbers[i] + ") = " + ours + " (Java: " + javas +")");
			difference[i] = Math.abs(ours - javas);
		}
		//CalculateError(difference);
		System.out.println();
	}
	
	static public void CalculateError(double[] difference) {
		double sum = 0;
		double averageError = 0;
		for(int i = 0; i < difference.length; i++) {
			sum = sum + difference[i];
		}
		averageError = sum / difference.length;
		System.out.println("Average error from Java's calculated value : " + averageError + ".");
		sum = 0;
	}
}
