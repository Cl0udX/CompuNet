package co.icesi.sudoku.controllers.dtos;

public class CellDTO {
    private int valueCurrent;
    private boolean isEditable;

    public CellDTO(int valueCurrent, boolean isEditable) {
        this.valueCurrent = valueCurrent;
        this.isEditable = isEditable;
    }

    public int getValueCurrent() {
        return valueCurrent;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setValueCurrent(int valueCurrent) {
        this.valueCurrent = valueCurrent;
    }
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
}