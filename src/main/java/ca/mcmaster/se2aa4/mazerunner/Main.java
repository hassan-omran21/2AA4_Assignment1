package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting Maze Runner with Design Patterns");

        Options options = new Options();
        options.addOption("i", "input", true, "Path to the maze input file");
        options.addOption("p", "path", true, "User-supplied path to verify (factorized or raw)");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            if (!cmd.hasOption("i")) {
                logger.error("Missing required option: -i <input_file>");
                System.err.println("Error: Missing required option '-i <input_file>'");
                return;
            }

            String inputFile = cmd.getOptionValue("i");
            Maze maze = new Maze(inputFile);

            int entryY = maze.findEntrance();
            int exitY = maze.findExit();

            logger.info("Maze entry point found at (0, {})", entryY);
            logger.info("Maze exit point found at ({}, {})", maze.getWidth() - 1, exitY);

            Explorer explorer = new Explorer(0, entryY, "EAST");
            Path path = new Path();
            path.addStep('F'); // initial step

            MazeSolvingStrategy solver = new RightHandSolver(exitY);
            solver.solveMaze(maze, explorer, path);

            String solverRawSteps = path.toRawString();
            String solverFactorizedSteps = path.factorizePath();

            if (cmd.hasOption("p")) {
                String userPathOriginal = cmd.getOptionValue("p");
                logger.info("User-supplied path (original): {}", userPathOriginal);

                String expandedUserPath = expandFactorizedPath(userPathOriginal);
                logger.info("User-supplied path (expanded): {}", expandedUserPath);

                if (expandedUserPath.equals(solverRawSteps)) {
                    System.out.println("User path is CORRECT!");
                } else {
                    System.out.println("User path is INCORRECT!");
                    System.out.println("Correct factorized path: " + solverFactorizedSteps);
                }

                logger.info("End of Maze Runner (with user path).");
            } else {
                System.out.println("Solver's factorized path: " + solverFactorizedSteps);
                logger.info("End of Maze Runner (no user path).");
            }
        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments: {}", e.getMessage());
            System.err.println("Error: " + e.getMessage());
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
                System.err.println("Warning: Unrecognized token: '" + token + "'. Ignoring it.");
            }
        }
        return sb.toString();
    }
}
