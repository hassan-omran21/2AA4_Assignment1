package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExplorerTest {

    @Test
    public void testMoveForward() {
        Explorer explorer = new Explorer(0, 1, "EAST");
        explorer.moveForward();
        assertEquals(1, explorer.getX());
        assertEquals(1, explorer.getY());
    }

    @Test
    public void testTurnLeftAndRight() {
        Explorer explorer = new Explorer(0, 0, "NORTH");
        explorer.turnLeft();
        assertEquals("WEST", explorer.getDirection());
        explorer.turnRight();
        assertEquals("NORTH", explorer.getDirection());
        explorer.turnRight();
        assertEquals("EAST", explorer.getDirection());
    }
}
