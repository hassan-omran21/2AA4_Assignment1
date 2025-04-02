package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class MazeSolverTest {

    // Helper method to create a temporary maze file.
    private File createTempMazeFile(String content) throws IOException {
        File tempFile = File.createTempFile("testMazeSolver", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Test
    public void testSolveMaze() throws IOException {
        // Create a simple 3x3 maze with entrance and exit on row 1.
        // Maze:
        // "###"
        // "   "
        // "###"
        String mazeContent = "###\n   \n###";
        File tempFile = createTempMazeFile(mazeContent);
        Main.Maze maze = new Main.Maze(tempFile.getAbsolutePath());
        int entrance = maze.findEntrance();
        int exit = maze.findExit();
        // Both entrance and exit should be at row 1.
        assertEquals(1, entrance);
        assertEquals(1, exit);

        Main.Explorer explorer = new Main.Explorer(0, entrance, "EAST");
        Main.Path path = new Main.Path();
        path.addStep('F'); // initial step

        Main.MazeSolver solver = new Main.MazeSolver(exit);
        solver.solveMaze(maze, explorer, path);

        // After solving, explorer's x should be maze.getWidth() - 1.
        assertEquals(maze.getWidth() - 1, explorer.getX());
        // Explorer's y should equal the exit row.
        assertEquals(exit, explorer.getY());
    }

    @Test
    public void testSolverPathNotEmpty() throws IOException {
        // Use the same simple maze.
        String mazeContent = "###\n   \n###";
        File tempFile = createTempMazeFile(mazeContent);
        Main.Maze maze = new Main.Maze(tempFile.getAbsolutePath());
        int entrance = maze.findEntrance();
        int exit = maze.findExit();

        Main.Explorer explorer = new Main.Explorer(0, entrance, "EAST");
        Main.Path path = new Main.Path();
        path.addStep('F'); // initial step

        Main.MazeSolver solver = new Main.MazeSolver(exit);
        solver.solveMaze(maze, explorer, path);

        String rawPath = path.toRawString();
        String factorizedPath = path.factorizePath();
        assertNotNull(rawPath);
        assertFalse(rawPath.isEmpty());
        assertNotNull(factorizedPath);
        assertFalse(factorizedPath.isEmpty());
    }
}
