package ca.mcmaster.se2aa4.mazerunner;

public class TurnLeftCommand implements Command {
    @Override
    public void execute(Explorer explorer, Path path) {
        explorer.turnLeft();
        path.addStep('L');
    }
}
