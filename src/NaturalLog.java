public class NaturalLog{
	/** The following method is an approximation to the natural logarithm function. It takes one 
	* argument; a double value whose natural logarithmic value needs to be found. The series is 
	* centered at one and is most precise in that vicinity
	*/
	static double calculate(double x)
	{
	// The following are initializations for the constant as well as all the intermediate values that 
	// will be used in this method.
	double constant = (x-1.0)/(x+1.0);
	double intermediate1 = 1.0;
	double intermediate2 = 1.0;
	double intermediate3 = 0.0;
	double result = 1.0;
	// added this since git wouldn't add the file, need a change
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
}