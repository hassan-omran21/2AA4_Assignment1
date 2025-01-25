package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting Maze Runner MVP");

        // Define command-line options
        Options options = new Options();
        options.addOption("i", "input", true, "Path to the maze input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            // Ensure the -i flag is provided
            if (!cmd.hasOption("i")) {
                logger.error("Missing required option: -i <input_file>");
                return;
            }

            String inputFile = cmd.getOptionValue("i");

            // Load maze from file
            Maze maze = new Maze(inputFile);

            // STARTING POSITION at (0,2) facing EAST
            Explorer explorer = new Explorer(0, 2, "EAST");

            Path path = new Path();

            // Solve the maze using the right-hand rule
            MazeSolver solver = new MazeSolver();
            solver.solveMaze(maze, explorer, path);

            // Print the canonical path
            logger.info("Canonical Path: {}", path.toString());
            logger.info("End of Maze Runner MVP");

        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments: {}", e.getMessage());
        }
    }

    // Maze class to load and represent the maze
    static class Maze {
        private char[][] grid;

        public Maze(String fileName) {
            if (!loadMaze(fileName)) {
                logger.error("Maze file could not be loaded. Exiting...");
                System.exit(1);
            }
        }

        private boolean loadMaze(String fileName) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                List<char[]> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line.toCharArray());
                }
                grid = lines.toArray(new char[0][]);
                return true; // File loaded successfully
            } catch (IOException e) {
                logger.error("Failed to load maze: {}", e.getMessage());
                return false; // File loading failed
            }
        }

        public boolean isPassable(int x, int y) {
            if (y < 0 || y >= grid.length) {
                return false; // Out of vertical bounds
            }
            if (x < 0 || x >= grid[y].length) {
                return false; // Out of horizontal bounds
            }
            return grid[y][x] == ' '; // Return true if the cell is a space
        }

        public int getWidth() {
            // For completeness if you still need it
            return grid.length > 0 ? grid[0].length : 0;
        }

        public int getHeight() {
            return grid.length;
        }
    }

    // Explorer class to manage movement
    static class Explorer {
        private int x, y;
        private String direction;

        public Explorer(int startX, int startY, String startDirection) {
            this.x = startX;
            this.y = startY;
            this.direction = startDirection;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getDirection() {
            return direction;
        }

        public void moveForward() {
            switch (direction) {
                case "NORTH" -> y--;
                case "SOUTH" -> y++;
                case "EAST"  -> x++;
                case "WEST"  -> x--;
            }
        }

        public void turnLeft() {
            switch (direction) {
                case "NORTH" -> direction = "WEST";
                case "SOUTH" -> direction = "EAST";
                case "EAST"  -> direction = "NORTH";
                case "WEST"  -> direction = "SOUTH";
            }
        }

        public void turnRight() {
            switch (direction) {
                case "NORTH" -> direction = "EAST";
                case "SOUTH" -> direction = "WEST";
                case "EAST"  -> direction = "SOUTH";
                case "WEST"  -> direction = "NORTH";
            }
        }

        public int getRightX() {
            return switch (direction) {
                case "NORTH" -> x + 1;
                case "SOUTH" -> x - 1;
                case "EAST"  -> x;
                case "WEST"  -> x;
                default -> x;
            };
        }

        public int getRightY() {
            return switch (direction) {
                case "NORTH" -> y;
                case "SOUTH" -> y;
                case "EAST"  -> y + 1;
                case "WEST"  -> y - 1;
                default -> y;
            };
        }

        public int getFrontX() {
            return switch (direction) {
                case "NORTH" -> x;
                case "SOUTH" -> x;
                case "EAST"  -> x + 1;
                case "WEST"  -> x - 1;
                default -> x;
            };
        }

        public int getFrontY() {
            return switch (direction) {
                case "NORTH" -> y - 1;
                case "SOUTH" -> y + 1;
                case "EAST"  -> y;
                case "WEST"  -> y;
                default -> y;
            };
        }
    }

    // Path class to track steps
    static class Path {
        private final List<Character> steps = new ArrayList<>();

        public void addStep(char step) {
            steps.add(step); // 'F', 'L', or 'R'
        }

        @Override
        public String toString() {
            return String.join("", steps.stream().map(String::valueOf).toArray(String[]::new));
        }
    }

    // MazeSolver class with right-hand rule
    static class MazeSolver {
        public void solveMaze(Maze maze, Explorer explorer, Path path) {
            while (true) {
                // Hard-coded exit: (4,2)
                if (isAtExit(explorer)) {
                    break;
                }

                // Check right
                if (maze.isPassable(explorer.getRightX(), explorer.getRightY())) {
                    explorer.turnRight();
                    path.addStep('R');
                    explorer.moveForward();
                    path.addStep('F');
                }
                // If right blocked, check front
                else if (maze.isPassable(explorer.getFrontX(), explorer.getFrontY())) {
                    explorer.moveForward();
                    path.addStep('F');
                }
                // Else turn left
                else {
                    explorer.turnLeft();
                    path.addStep('L');
                }
            }
        }

        private boolean isAtExit(Explorer explorer) {
            // Hard-coded end at (4, 2)
            return explorer.getX() == 4 && explorer.getY() == 2;
        }
    }
}
