package ca.mcmaster.se2aa4.mazerunner;

public class MoveForwardCommand implements Command {
    @Override
    public void execute(Explorer explorer, Path path) {
        explorer.moveForward();
        path.addStep('F');
    }
}
