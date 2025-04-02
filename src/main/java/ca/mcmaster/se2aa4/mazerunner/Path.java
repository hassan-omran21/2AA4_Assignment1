package ca.mcmaster.se2aa4.mazerunner;

import java.util.ArrayList;
import java.util.List;

public class Path {
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
