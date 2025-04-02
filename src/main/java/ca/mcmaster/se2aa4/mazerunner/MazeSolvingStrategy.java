package ca.mcmaster.se2aa4.mazerunner;

public interface MazeSolvingStrategy {
    void solveMaze(Maze maze, Explorer explorer, Path path);
}
