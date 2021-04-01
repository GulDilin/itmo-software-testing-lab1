package model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {

    @ParameterizedTest(name = "Create position for ({0} {1})")
    @CsvFileSource(resources = "test_position_get_distance.csv")
    void createPositionTest(int x, int y) {
        Position p = new Position(x, y);
        assertEquals(x, p.getX());
        assertEquals(y, p.getY());
    }

    @ParameterizedTest(name = "Count distance for ({0} {1}) and ({2} {3})")
    @CsvFileSource(resources = "test_position_get_distance.csv")
    void getDistanceTest(int x1, int y1, int x2, int y2, double expected) {
        Position p1 = new Position(x1, y1);
        Position p2 = new Position(x2, y2);
        assertEquals(expected, p1.getDistance(p2));
        assertEquals(expected, p2.getDistance(p1));
    }

}

