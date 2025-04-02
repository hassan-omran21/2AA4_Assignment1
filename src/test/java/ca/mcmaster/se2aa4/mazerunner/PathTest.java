package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PathTest {

    @Test
    public void testToRawString() {
        Path path = new Path();
        path.addStep('F');
        path.addStep('R');
        path.addStep('F');
        String raw = path.toRawString();
        assertEquals("FRF", raw);
    }

    @Test
    public void testFactorizePath() {
        Path path = new Path();
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
