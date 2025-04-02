package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    private static final Logger logger = LogManager.getLogger(Maze.class);
    private char[][] grid;

    public Maze(String fileName) {
        if (!loadMaze(fileName)) {
            logger.error("Maze file could not be loaded. Exiting...");
            System.err.println("Error: Maze file could not be loaded. Exiting...");
            System.exit(1);
        }
    }

    private boolean loadMaze(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            List<char[]> lines = new ArrayList<>();
            String line;
            int targetWidth = 0;
            if ((line = reader.readLine()) != null) {
                targetWidth = line.length();
                lines.add(line.toCharArray());
            }
            while ((line = reader.readLine()) != null) {
                if (line.length() < targetWidth) {
                    line = padRowWithSpaces(line, targetWidth);
                }
                lines.add(line.toCharArray());
            }
            grid = lines.toArray(new char[0][]);
            return true;
        } catch (IOException e) {
            logger.error("Failed to load maze: {}", e.getMessage());
            System.err.println("Error: Failed to load maze: " + e.getMessage());
            return false;
        }
    }

    private String padRowWithSpaces(String row, int targetWidth) {
        StringBuilder paddedRow = new StringBuilder(row);
        while (paddedRow.length() < targetWidth) {
            paddedRow.append(' ');
        }
        return paddedRow.toString();
    }

    public int findEntrance() {
        for (int y = 0; y < grid.length; y++) {
            if (grid[y][0] == ' ') {
                return y;
            }
        }
        logger.error("No entrance found on the left side of the maze.");
        System.err.println("Error: No entrance found on the left side of the maze.");
        throw new IllegalStateException("No valid entrance found on the left side of the maze.");
    }

    public int findExit() {
        for (int y = 0; y < grid.length; y++) {
            if (grid[y][grid[y].length - 1] == ' ') {
                return y;
            }
        }
        logger.error("No exit found on the right side of the maze.");
        System.err.println("Error: No exit found on the right side of the maze.");
        throw new IllegalStateException("No valid exit found on the right side of the maze.");
    }

    public boolean isPassable(int x, int y) {
        if (y < 0 || y >= grid.length) return false;
        if (x < 0 || x >= grid[y].length) return false;
        return (grid[y][x] == ' ');
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }
}
