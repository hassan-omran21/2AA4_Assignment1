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
        String mazeContent = "###\n   \n###";
        File tempFile = createTempMazeFile(mazeContent);
        Maze maze = new Maze(tempFile.getAbsolutePath());
        int entrance = maze.findEntrance();
        int exit = maze.findExit();
        assertEquals(1, entrance);
        assertEquals(1, exit);

        Explorer explorer = new Explorer(0, entrance, "EAST");
        Path path = new Path();
        path.addStep('F'); // initial step

        RightHandSolver solver = new RightHandSolver(exit);
        solver.solveMaze(maze, explorer, path);

        assertEquals(maze.getWidth() - 1, explorer.getX());
        assertEquals(exit, explorer.getY());
    }

    @Test
    public void testSolverPathNotEmpty() throws IOException {
        String mazeContent = "###\n   \n###";
        File tempFile = createTempMazeFile(mazeContent);
        Maze maze = new Maze(tempFile.getAbsolutePath());
        int entrance = maze.findEntrance();
        int exit = maze.findExit();

        Explorer explorer = new Explorer(0, entrance, "EAST");
        Path path = new Path();
        path.addStep('F'); // initial step

        RightHandSolver solver = new RightHandSolver(exit);
        solver.solveMaze(maze, explorer, path);

        String rawPath = path.toRawString();
        String factorizedPath = path.factorizePath();
        assertNotNull(rawPath);
        assertFalse(rawPath.isEmpty());
        assertNotNull(factorizedPath);
        assertFalse(factorizedPath.isEmpty());
    }
}
