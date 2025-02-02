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

        Options options = new Options();
        options.addOption("i", "input", true, "Path to the maze input file");
        options.addOption("p", "path", true, "User-supplied path to verify (factorized or raw)");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            if (!cmd.hasOption("i")) {
                logger.error("Missing required option: -i <input_file>");
                return;
            }

            String inputFile = cmd.getOptionValue("i");
            Maze maze = new Maze(inputFile);

            int entryY = maze.findEntrance();
            int exitY = maze.findExit();

            logger.info("Maze entry point found at (0, {})", entryY);
            logger.info("Maze exit point found at ({}, {})\n", maze.getWidth() - 1, exitY);

            Explorer solverExplorer = new Explorer(0, entryY, "EAST");
            Path solverPath = new Path();
            solverPath.addStep('F');

            MazeSolver solver = new MazeSolver(exitY);
            solver.solveMaze(maze, solverExplorer, solverPath);

            String solverRawSteps = solverPath.toRawString();
            String solverFactorizedSteps = solverPath.factorizePath();

            if (cmd.hasOption("p")) {
                String userPathOriginal = cmd.getOptionValue("p");
                logger.info("User-supplied path (original): {}", userPathOriginal);

                String expandedUserPath = expandFactorizedPath(userPathOriginal);
                logger.info("User-supplied path (expanded): {}\n", expandedUserPath);

                if (expandedUserPath.equals(solverRawSteps)) {
                    logger.info("User path is CORRECT! It matches the solver's raw path.");
                } else {
                    logger.info("User path is INCORRECT.");
                    logger.info("Solver's raw path  : {}", solverRawSteps);
                    logger.info("Solver's factored : {}", solverFactorizedSteps);
                }
                logger.info("End of Maze Runner MVP (with user path).");
            } else {
                logger.info("Solver's raw (unfactored) path: {}", solverRawSteps);
                logger.info("Solver's factorized path: {}\n", solverFactorizedSteps);
                logger.info("End of Maze Runner MVP (no user path).");
            }

        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments: {}", e.getMessage());
        }
    }

    private static String expandFactorizedPath(String userInput) {
        String[] tokens = userInput.split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String token : tokens) {
            if (token.matches("\\d+[FRL]")) {
                char stepChar = token.charAt(token.length() - 1);
                int count = Integer.parseInt(token.substring(0, token.length() - 1));
                for (int i = 0; i < count; i++) {
                    sb.append(stepChar);
                }
            } else if (token.matches("[FRL]+")) {
                sb.append(token);
            } else {
                logger.warn("Ignoring unrecognized token: '{}'. Expected patterns like '2F' or 'FF'.", token);
            }
        }
        return sb.toString();
    }

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
            throw new IllegalStateException("No valid entrance found on the left side of the maze.");
        }

        public int findExit() {
            for (int y = 0; y < grid.length; y++) {
                if (grid[y][grid[y].length - 1] == ' ') {
                    return y;
                }
            }
            logger.error("No exit found on the right side of the maze.");
            throw new IllegalStateException("No valid exit found on the right side of the maze.");
        }

        public boolean isPassable(int x, int y) {
            if (y < 0 || y >= grid.length) {
                return false;
            }
            if (x < 0 || x >= grid[y].length) {
                return false;
            }
            return grid[y][x] == ' ';
        }

        public int getWidth() {
            return grid[0].length;
        }

        public int getHeight() {
            return grid.length;
        }
    }

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
                case "EAST" -> x++;
                case "WEST" -> x--;
            }
        }

        public void turnLeft() {
            switch (direction) {
                case "NORTH" -> direction = "WEST";
                case "SOUTH" -> direction = "EAST";
                case "EAST" -> direction = "NORTH";
                case "WEST" -> direction = "SOUTH";
            }
        }

        public void turnRight() {
            switch (direction) {
                case "NORTH" -> direction = "EAST";
                case "SOUTH" -> direction = "WEST";
                case "EAST" -> direction = "SOUTH";
                case "WEST" -> direction = "NORTH";
            }
        }

        public int getRightX() {
            return switch (direction) {
                case "NORTH" -> x + 1;
                case "SOUTH" -> x - 1;
                case "EAST" -> x;
                case "WEST" -> x;
                default -> x;
            };
        }

        public int getRightY() {
            return switch (direction) {
                case "NORTH" -> y;
                case "SOUTH" -> y;
                case "EAST" -> y + 1;
                case "WEST" -> y - 1;
                default -> y;
            };
        }

        public int getFrontX() {
            return switch (direction) {
                case "NORTH" -> x;
                case "SOUTH" -> x;
                case "EAST" -> x + 1;
                case "WEST" -> x - 1;
                default -> x;
            };
        }

        public int getFrontY() {
            return switch (direction) {
                case "NORTH" -> y - 1;
                case "SOUTH" -> y + 1;
                case "EAST" -> y;
                case "WEST" -> y;
                default -> y;
            };
        }

        public int getLeftX() {
            return switch (direction) {
                case "NORTH" -> x - 1;
                case "SOUTH" -> x + 1;
                case "EAST" -> x;
                case "WEST" -> x;
                default -> x;
            };
        }

        public int getLeftY() {
            return switch (direction) {
                case "NORTH" -> y;
                case "SOUTH" -> y;
                case "EAST" -> y - 1;
                case "WEST" -> y + 1;
                default -> y;
            };
        }
    }

    static class Path {
        private final List<Character> steps = new ArrayList<>();

        public void addStep(char step) {
            steps.add(step);
        }

        public String toRawString() {
            StringBuilder sb = new StringBuilder();
            for (char c : steps) {
                sb.append(c);
            }
            return sb.toString();
        }

        public String factorizePath() {
            if (steps.isEmpty()) return "";

            StringBuilder result = new StringBuilder();
            int count = 1;
            char current = steps.get(0);

            for (int i = 1; i < steps.size(); i++) {
                char next = steps.get(i);
                if (next == current) {
                    count++;
                } else {
                    result.append(count > 1 ? count : "").append(current).append(" ");
                    current = next;
                    count = 1;
                }
            }
            result.append(count > 1 ? count : "").append(current);
            return result.toString().trim();
        }
    }

    static class MazeSolver {
        private final int exitY;

        public MazeSolver(int exitY) {
            this.exitY = exitY;
        }

        public void solveMaze(Maze maze, Explorer explorer, Path path) {
            while (true) {
                if (isAtExit(maze, explorer)) {
                    break;
                }

                boolean rightPassable = maze.isPassable(explorer.getRightX(), explorer.getRightY());
                boolean frontPassable = maze.isPassable(explorer.getFrontX(), explorer.getFrontY());
                boolean leftPassable = maze.isPassable(explorer.getLeftX(), explorer.getLeftY());

                if (rightPassable) {
                    explorer.turnRight();
                    path.addStep('R');
                    explorer.moveForward();
                    path.addStep('F');
                } else if (frontPassable) {
                    explorer.moveForward();
                    path.addStep('F');
                } else if (leftPassable) {
                    explorer.turnLeft();
                    path.addStep('L');
                    explorer.moveForward();
                    path.addStep('F');
                } else {
                    explorer.turnRight();
                    path.addStep('R');
                    explorer.turnRight();
                    path.addStep('R');
                    explorer.moveForward();
                    path.addStep('F');
                }
            }
        }

        private boolean isAtExit(Maze maze, Explorer explorer) {
            return explorer.getX() == maze.getWidth() - 1 && explorer.getY() == exitY;
        }
    }
}
