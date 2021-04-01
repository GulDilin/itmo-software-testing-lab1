package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AreaTest {
    private Area area;
    private final int maxMovables = 3;

    @BeforeEach
    public void setup() {
        area = new Area(maxMovables);
    }

    @DisplayName("Add human")
    @Test
    public void testAddHuman() {
        Human human = new Human(new Position(0, 0), "Ivan", 100);
        assertTrue(area.addMovable(human));
    }

    @DisplayName("Add mechanism")
    @Test
    public void testAddMechanism() {
        Mechanism mechanism = new Mechanism(new Position(0, 0), "RoboIvan", 100);
        assertTrue(area.addMovable(mechanism));
    }

    @DisplayName("Amount")
    @Test
    public void testAmount() {
        Mechanism mechanism1 = new Mechanism(new Position(0, 0), "RoboIvan1", 100);
        Mechanism mechanism2 = new Mechanism(new Position(0, 0), "RoboIvan2", 100);
        Mechanism mechanism3 = new Mechanism(new Position(0, 0), "RoboIvan3", 100);
        Mechanism mechanism4 = new Mechanism(new Position(0, 0), "RoboIvan4", 100);
        assertTrue(area.addMovable(mechanism1), "Add 1");
        assertTrue(area.addMovable(mechanism2), "Add 2");
        assertTrue(area.addMovable(mechanism3), "Add 3");
        assertFalse(area.addMovable(mechanism4), "Add 4");
    }
}
