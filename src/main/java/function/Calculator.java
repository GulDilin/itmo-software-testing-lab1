package function;

import java.math.BigInteger;

public class Calculator {
    public static long factorial(int x) {
        long result = 1;
        for (int i = 1; i <= x; i++) {
            result *= i;
        }
        return result;
    }

    public static double cos(double x) {
        double current = 1D;

        for (int i = 1; i < 20; i++) {
            int multiplier = i % 2 == 0 ? 1 : -1;
            int power = i + i;
            double nominator = Math.pow(x, power);
            long denominator = factorial(power);
            current += multiplier * nominator / denominator;
        }

        if (current > 1) return 1;
        if (current < -1) return -1;
        return current;
    }
}
