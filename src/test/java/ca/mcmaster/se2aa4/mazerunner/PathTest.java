package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PathTest {

    @Test
    public void testToRawString() {
        Main.Path path = new Main.Path();
        path.addStep('F');
        path.addStep('R');
        path.addStep('F');
        String raw = path.toRawString();
        assertEquals("FRF", raw);
    }

    @Test
    public void testFactorizePath() {
        Main.Path path = new Main.Path();
        // Add steps: F, F, F, L, F, F
        path.addStep('F');
        path.addStep('F');
        path.addStep('F');
        path.addStep('L');
        path.addStep('F');
        path.addStep('F');
        String factorized = path.factorizePath();
        // Expected factorized form: "3F L 2F"
        assertEquals("3F L 2F", factorized);
    }
}
