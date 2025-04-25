package dev.harpia.sudoku.model;

import java.util.List;
import static dev.harpia.sudoku.model.GameStatusEnum.*;

public class Board {

    private List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public GameStatusEnum getGameStatus() {
        if (spaces.stream().flatMap(List::stream).noneMatch(s -> !s.isFixed() && s.getActualValue() != null)) {
            return NOT_STARTED;
        }

        return spaces.stream().flatMap(List::stream).anyMatch(s -> s.getActualValue() == null) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors() {
        if (getGameStatus() == NOT_STARTED) {
            return false;
        }

        return spaces.stream().flatMap(List::stream).anyMatch(s -> s.getActualValue() != null && s.getActualValue() != s.getExpectedValue());
    }

    public boolean changeSpaceValue(int row, int column, Integer value) {
        if (spaces.get(row).get(column).isFixed()) {
            return false;
        }

        spaces.get(row).get(column).setActualValue(value);
        return true;
    }

    public boolean clearSpaceValue(int row, int column) {
        if (spaces.get(row).get(column).isFixed()) {
            return false;
        }

        spaces.get(row).get(column).clearSpace();
        return true;
    }

    public void reset() {
        spaces.forEach(r -> r.forEach(Space::clearSpace));
    }

    public boolean isGameFinished() {
        return !hasErrors() && getGameStatus() == COMPLETE;
    }

}
