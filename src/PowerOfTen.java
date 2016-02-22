public class PowerOfTen {
	
	//The following method is an approximation to the natural logarithm function. It takes one argument; a double value whose natural logarithmic value needs to be found.
	static double naturalLog(double x)
	{
	//The following are initializations for the constant as well as all the intermediate values that will be used in this method.
	double constant = (x-1.0)/(x+1.0);
	double intermediate1 = 1.0;
	double intermediate2 = 1.0;
	double intermediate3 = 0.0;
	double result = 1.0;
	/* A for loop that does the approximation. The following equation is what is being summed up by the for loop:
	 * 
	 * Ln(x) = 2[(x-1)/(x+1) + (1/3)*((x-1)/(x+1))^3 + (1/5)*((x-1)/(x+1))^5 + (1/7)*((x-1)/(x+1))^7...]
	 * Source: http://www.math.com/tables/expansion/log.htm
	 * For the outer for loop, summing up the terms until n = 50.0 is enough to give a relative error of 3.953*10^-9 % based on the ln(10) example. 
	 * 
	 */
		for (double n=1.0;n<=50.0;n++)
		{
			intermediate1 = 1.0;
	//The inner loop needs to raise the constant value to the (2n-1). This is what's happening here.
			for (double z=1.0;z<=(2.0*n-1.0);z=z+1.0)
			{
				double temp1=(x-1.0)/(x+1.0);
				constant=temp1;
				intermediate1 = intermediate1*constant;
			}
			
			intermediate2 =intermediate1*(1.0/(2.0*n-1.0));
			intermediate3 = intermediate3+intermediate2;
		}
	//Finally, the result of all the intermediate steps is multiplied by 2.0 to get the final result.
		result = 2.0*intermediate3;
		return result;
		}
	
	// This is the value of ln(10.0). This is defined as a final value since it does not change. 
	static final double NaturalLog10  = naturalLog(10.0); 
		
	// This is the method that approximates the function 10^x. It takes one double value (y) and then return the approximation of 10^y. 
	static double powerOfTen(double y){
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
			return (1.0/(ExpFunction.calculate((-1.0)*y*NaturalLog10)));
		}
		else if (y==0)
		{
			return (1.0);
		}
		else 
		{
			return ((ExpFunction.calculate(y*NaturalLog10)));
		}
	}
}
