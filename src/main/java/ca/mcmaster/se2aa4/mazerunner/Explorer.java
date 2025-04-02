package ca.mcmaster.se2aa4.mazerunner;

public class Explorer {
    private int x, y;
    private String direction;

    public Explorer(int startX, int startY, String startDirection) {
        this.x = startX;
        this.y = startY;
        this.direction = startDirection;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getDirection() { return direction; }

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
            default      -> x;
        };
    }

    public int getRightY() {
        return switch (direction) {
            case "NORTH" -> y;
            case "SOUTH" -> y;
            case "EAST"  -> y + 1;
            case "WEST"  -> y - 1;
            default      -> y;
        };
    }

    public int getFrontX() {
        return switch (direction) {
            case "NORTH" -> x;
            case "SOUTH" -> x;
            case "EAST"  -> x + 1;
            case "WEST"  -> x - 1;
            default      -> x;
        };
    }

    public int getFrontY() {
        return switch (direction) {
            case "NORTH" -> y - 1;
            case "SOUTH" -> y + 1;
            case "EAST"  -> y;
            case "WEST"  -> y;
            default      -> y;
        };
    }

    public int getLeftX() {
        return switch (direction) {
            case "NORTH" -> x - 1;
            case "SOUTH" -> x + 1;
            case "EAST"  -> x;
            case "WEST"  -> x;
            default      -> x;
        };
    }

    public int getLeftY() {
        return switch (direction) {
            case "NORTH" -> y;
            case "SOUTH" -> y;
            case "EAST"  -> y - 1;
            case "WEST"  -> y + 1;
            default      -> y;
        };
    }
}
