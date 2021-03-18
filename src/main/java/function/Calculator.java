package function;

public class Calculator {
    public static long factorial(int x) {
        long result = 1;
        for (int i = 1; i <= x; i++) {
            result *= i;
        }
        return result;
    }

    public static double cos(double x) {
        double result = 1D;
        double current = 1D;
        double previous = 0;
        final double ACCURACY = 1e-4D;

        for (int i = 1; Math.abs(current - previous) > ACCURACY && i < 1000 ; i++) {
            previous = current;
            current *= ( x * x * -1 );
            current /= ( (2 * i) * (2 * i - 1) );
            result += current;
        }

        if (result > 1) return 1;
        if (result < -1) return -1;
        return result;
    }
}
