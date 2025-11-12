package co.icesi.sudoku.model;

public class Cell {

    private int valueCorrect;
    private int valueCurrent;
    private boolean isEditable;

    public Cell(int value) {
        this.valueCorrect = value;
        this.valueCurrent = value;
        this.isEditable = false;
    }

    public int getValueCorrect() {
        return valueCorrect;
    }

    public void setValueCorrect(int valueCorrect) {
        this.valueCorrect = valueCorrect;
    }

    public int getValueCurrent() {
        return valueCurrent;
    }

    public void setValueCurrent(int valueCurrent) {
        this.valueCurrent = valueCurrent;
    }

    public boolean isEditable() {
        return isEditable;
    }
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
}
