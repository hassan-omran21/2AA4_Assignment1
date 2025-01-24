package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("Starting Maze Runner");

        Options options = new Options();
        options.addOption("i", true, "Input maze file");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("i")) {
                String mazeFile = cmd.getOptionValue("i");
                logger.info("Reading the maze from file: {}", mazeFile);

                try (BufferedReader reader = new BufferedReader(new FileReader(mazeFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        for (int idx = 0; idx < line.length(); idx++) {
                            if (line.charAt(idx) == '#') {
                                System.out.print("WALL ");
                            } else if (line.charAt(idx) == ' ') {
                                System.out.print("PASS ");
                            }
                        }
                        System.out.println();
                    }
                }
            } else {
                logger.error("No input file provided. Use the -i flag to specify a maze file.");
            }
        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments", e);
        } catch (IOException e) {
            logger.error("An error occurred while reading the maze file", e);
        }

        logger.info("End of Maze Runner");
    }
}
