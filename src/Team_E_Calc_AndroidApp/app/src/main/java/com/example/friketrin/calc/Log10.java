package com.example.friketrin.calc;

public class Log10 {
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
			exponent = floor + (NaturalLog.calculate(temp) / NaturalLog.calculate(10));
			
			return exponent;
		}
	}
}
