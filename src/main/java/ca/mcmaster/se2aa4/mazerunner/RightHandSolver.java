package ca.mcmaster.se2aa4.mazerunner;

public class RightHandSolver implements MazeSolvingStrategy {
    private final int exitY;

    public RightHandSolver(int exitY) {
        this.exitY = exitY;
    }

    @Override
    public void solveMaze(Maze maze, Explorer explorer, Path path) {
        while (!isAtExit(maze, explorer)) {
            boolean rightPassable = maze.isPassable(explorer.getRightX(), explorer.getRightY());
            boolean frontPassable = maze.isPassable(explorer.getFrontX(), explorer.getFrontY());
            boolean leftPassable = maze.isPassable(explorer.getLeftX(), explorer.getLeftY());

            if (rightPassable) {
                new TurnRightCommand().execute(explorer, path);
                new MoveForwardCommand().execute(explorer, path);
            } else if (frontPassable) {
                new MoveForwardCommand().execute(explorer, path);
            } else if (leftPassable) {
                new TurnLeftCommand().execute(explorer, path);
                new MoveForwardCommand().execute(explorer, path);
            } else {
                // Dead end: turn around (two right turns)
                new TurnRightCommand().execute(explorer, path);
                new TurnRightCommand().execute(explorer, path);
                new MoveForwardCommand().execute(explorer, path);
            }
        }
    }

    private boolean isAtExit(Maze maze, Explorer explorer) {
        return explorer.getX() == maze.getWidth() - 1 && explorer.getY() == exitY;
    }
}
