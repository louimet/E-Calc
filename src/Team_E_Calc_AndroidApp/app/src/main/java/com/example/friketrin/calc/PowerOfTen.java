package com.example.friketrin.calc;

public class PowerOfTen {
		
	// This is the value of ln(10.0). This is defined as a final value since it does not change. 
	static double naturalLog10  = NaturalLog.calculate(10.0);
		
	// This is the method that approximates the function 10^x. It takes one double value (y) and then return the approximation of 10^y. 
	static double calculate(double y){
	/* Using the rules of natural logarithms we can see that:
	 * 
	 * 10^x = e^(x*ln(10)).
	 * Sine e^(ln(10)) = 10. 
	 * 
	 *
	 * If the power to which 10.0 is raised is negative, the inverse must be taken as such:
	 * 10^-x = (1/(10^x)).
	 * 
	 * If the power to which 10.0 is raised is equal to 0.0, the answer return 1.0.
	 * 
	 * If the power to which 10.0 is raised is bigger than 0.0, the answer is 10^x = e^(x*ln(10)).
	 * The e^x function is based on the implementation of calculate(double x) by Yuanwen Qin.  
	*/	
		if (y<0)
		{
			return (1.0/(ExpFunction.calculate((-1.0)*y*naturalLog10)));
		}
		else if (y==0)
		{
			return (1.0);
		}
		else 
		{
			return ((ExpFunction.calculate(y*naturalLog10)));
		}
	}
}
