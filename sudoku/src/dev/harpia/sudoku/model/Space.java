package dev.harpia.sudoku.model;

public class Space {

    private Integer actualValue;
    private int expectedValue;
    private boolean fixed;

    public Space(int expectedValue, boolean fixed) {
        this.expectedValue = expectedValue;
        this.fixed = fixed;
        if (fixed) {
            this.actualValue = expectedValue;
        }
    }

    public Integer getActualValue() {
        return actualValue;
    }

    public void setActualValue(Integer actualValue) {
        if (fixed) return;
        this.actualValue = actualValue;
    }

    public void clearSpace() {
        setActualValue(null);
    }

    public int getExpectedValue() {
        return expectedValue;
    }

    public boolean isFixed() {
        return fixed;
    }

}
