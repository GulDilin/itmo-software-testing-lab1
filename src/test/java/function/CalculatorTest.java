package function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Calculator tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalculatorTest {

    @ParameterizedTest(name = "Calculate cos({0})")
    @CsvFileSource(resources = "/calculator_test_data_pi1.csv")
    void dataPITest(double expected, int nominator, int denominator) {
        double input = (nominator * Math.PI) / denominator;
        double actual = Calculator.cos(input);
        final double ACCURACY = 1e-3D;
        System.out.printf("expected = %9f | nominator = %3d | denominator = %3d | input = %9f | actual = %9f\n",
                expected, nominator, denominator, input, actual);
        assertEquals(expected, actual, ACCURACY);
    }

    @ParameterizedTest(name = "Calculate cos({0})")
    @CsvFileSource(resources = "/calculator_test_data.csv")
    void dataTest(double expected, double inputDeg) {
        double input = inputDeg / 180 * Math.PI;
        double actual = Calculator.cos(input);
        final double ACCURACY = 1e-3D;
        System.out.printf("expected = %9f | inputDegrees = %9f | inputRad = %9f | actual = %9f\n",
                expected, inputDeg, input, actual);
        assertEquals(expected, actual, ACCURACY);
    }
}
