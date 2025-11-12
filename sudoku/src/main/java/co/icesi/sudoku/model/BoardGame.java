package co.icesi.sudoku.model;

import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class BoardGame {

    private Cell[][] cells;
    private int size;
    private int emptyCells;
    private boolean surrendered;

    public BoardGame(int size, int emptyCells) {
        this.size = size;
        this.emptyCells = emptyCells;
        this.surrendered = false;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getEmptyCells() {
        return emptyCells;
    }

    public void setEmptyCells(int emptyCells) {
        this.emptyCells = emptyCells;
    }

    public boolean isSurrendered() {
        return surrendered;
    }

    public void setSurrendered(boolean surrendered) {
        this.surrendered = surrendered;
    }

    public void generatePlayableBoard() {
        setSurrendered(false);
        cells = new Cell[size * size][size * size];
        fillBoard(0, 0, size);
        removeCells(size, emptyCells);
    }

    private boolean fillBoard(int row, int col, int size) {
        if (row == size * size)
            return true;
        int nextRow = col == size * size - 1 ? row + 1 : row;
        int nextCol = col == size * size - 1 ? 0 : col + 1;

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= size * size; i++)
            numbers.add(i);
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (isSafe(row, col, num, size)) {
                cells[row][col] = new Cell(num);
                if (fillBoard(nextRow, nextCol, size))
                    return true;
                cells[row][col] = new Cell(0);
            }
        }
        return false;
    }

    private boolean isSafe(int row, int col, int num, int size) {
        for (int i = 0; i < size * size; i++) {
            if (cells[row][i] != null && cells[row][i].getValueCorrect() == num)
                return false;
            if (cells[i][col] != null && cells[i][col].getValueCorrect() == num)
                return false;
        }
        int boxRow = row - row % size;
        int boxCol = col - col % size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int r = boxRow + i;
                int c = boxCol + j;
                if (cells[r][c] != null && cells[r][c].getValueCorrect() == num)
                    return false;
            }
        }
        return true;
    }

    private void removeCells(int size, int emptyCells) {
        Random rand = new Random();
        int totalCells = size * size * size * size;
        int removed = 0;
        while (removed < emptyCells) {
            int i = rand.nextInt(size * size);
            int j = rand.nextInt(size * size);
            if (cells[i][j].getValueCurrent() != 0) {
                cells[i][j].setValueCurrent(0);
                cells[i][j].setEditable(true);
                removed++;
            }
        }
    }

    public void setValue(int row, int col, int value) {
        if (row >= 0 && row < size * size && col >= 0 && col < size * size) {
            cells[row][col].setValueCurrent(value);
        }
    }

    public boolean isComplete() {
        for (int i = 0; i < size * size; i++) {
            for (int j = 0; j < size * size; j++) {
                if (cells[i][j].getValueCurrent() == 0)
                    return false;
            }
        }
        return true;
    }

    public boolean isValidSolution() {
        if (!isComplete())
            return false;

        if (isSurrendered())
            return false;
        int n = size * size;

        for (int i = 0; i < n; i++) {
            boolean[] seen = new boolean[n + 1];
            for (int j = 0; j < n; j++) {
                int val = cells[i][j].getValueCurrent();
                if (val < 1 || val > n || seen[val])
                    return false;
                seen[val] = true;
            }
        }

        for (int j = 0; j < n; j++) {
            boolean[] seen = new boolean[n + 1];
            for (int i = 0; i < n; i++) {
                int val = cells[i][j].getValueCurrent();
                if (val < 1 || val > n || seen[val])
                    return false;
                seen[val] = true;
            }
        }

        for (int boxRow = 0; boxRow < size; boxRow++) {
            for (int boxCol = 0; boxCol < size; boxCol++) {
                boolean[] seen = new boolean[n + 1];
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        int r = boxRow * size + i;
                        int c = boxCol * size + j;
                        int val = cells[r][c].getValueCurrent();
                        if (val < 1 || val > n || seen[val])
                            return false;
                        seen[val] = true;
                    }
                }
            }
        }
        return true;
    }

    public void completeGame(){
        setSurrendered(true);
        for (int i = 0; i < size * size; i++) {
            for (int j = 0; j < size * size; j++) {
                cells[i][j].setValueCurrent(cells[i][j].getValueCorrect());
            }
        }
    }
}