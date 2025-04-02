package ca.mcmaster.se2aa4.mazerunner;

public class TurnRightCommand implements Command {
    @Override
    public void execute(Explorer explorer, Path path) {
        explorer.turnRight();
        path.addStep('R');
    }
}
