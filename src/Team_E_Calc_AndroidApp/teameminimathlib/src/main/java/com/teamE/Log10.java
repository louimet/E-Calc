package com.teamE;

public class Log10 {
	static public double calculate(double number) {
		double exponent;
		
		if(number <= 0) {
			double badDouble = Double.NaN;
			exponent = badDouble;
		} else if(number == 1) {
			exponent = 0;
		} else if(number > 0 && number < 1) {
			exponent = NaturalLog.calculate(number) / NaturalLog.calculate(10);
		} else {
			exponent = 0;
			for(;number > 1; exponent++) {
				number = number / 10;
			}
			exponent = exponent + (NaturalLog.calculate(number) / NaturalLog.calculate(10));
		}
		return exponent;
	}
}
