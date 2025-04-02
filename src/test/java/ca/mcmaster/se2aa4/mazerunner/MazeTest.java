package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class MazeTest {

    // Helper method to create a temporary maze file.
    private File createTempMazeFile(String content) throws IOException {
        File tempFile = File.createTempFile("testMaze", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Test
    public void testFindEntrance() throws IOException {
        // Maze with entrance on the left (row 1 has a space in the first column)
        String mazeContent = "##\n  \n##";
        File tempFile = createTempMazeFile(mazeContent);
        Main.Maze maze = new Main.Maze(tempFile.getAbsolutePath());
        int entrance = maze.findEntrance();
        assertEquals(1, entrance);
    }

    @Test
    public void testFindExit() throws IOException {
        // Maze with exit on the right (row 1 has a space at the end)
        String mazeContent = "##\n  \n##";
        File tempFile = createTempMazeFile(mazeContent);
        Main.Maze maze = new Main.Maze(tempFile.getAbsolutePath());
        int exit = maze.findExit();
        assertEquals(1, exit);
    }

    @Test
    public void testIsPassable() throws IOException {
        // Maze: "##", "  ", "##"
        String mazeContent = "##\n  \n##";
        File tempFile = createTempMazeFile(mazeContent);
        Main.Maze maze = new Main.Maze(tempFile.getAbsolutePath());
        // Row 1 should be passable
        assertTrue(maze.isPassable(0, 1));
        assertTrue(maze.isPassable(1, 1));
        // Row 0 is not passable
        assertFalse(maze.isPassable(0, 0));
        // Out-of-bounds coordinates
        assertFalse(maze.isPassable(-1, 1));
        assertFalse(maze.isPassable(1, -1));
    }

    @Test
    public void testRowPadding() throws IOException {
        // Test that rows shorter than the first row are padded.
        String mazeContent = "####\n##";
        File tempFile = createTempMazeFile(mazeContent);
        Main.Maze maze = new Main.Maze(tempFile.getAbsolutePath());
        // The width should match the first row (4 characters)
        assertEquals(4, maze.getWidth());
        assertEquals(2, maze.getHeight());
    }

    @Test
    public void testImpossibleMazeNoEntrance() throws IOException {
        // This maze has no opening on the left border, so no valid entrance.
        String mazeContent = "#####\n#   #\n#   #\n#   #\n#####";
        File tempFile = createTempMazeFile(mazeContent);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new Main.Maze(tempFile.getAbsolutePath()).findEntrance();
        });
        assertTrue(exception.getMessage().contains("No valid entrance"));
    }

}
