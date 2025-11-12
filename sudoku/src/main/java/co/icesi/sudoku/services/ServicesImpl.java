package co.icesi.sudoku.services;

import co.icesi.sudoku.controllers.dtos.CellDTO;
import co.icesi.sudoku.model.BoardGame;
import co.icesi.sudoku.model.Cell;

public class ServicesImpl {

    private BoardGame game;

    public ServicesImpl() {
        initGame();
    }

    public BoardGame getGame() {
        return game;
    }

    // INIT_GAME
    public void initGame() {
        game = new BoardGame(3, 1);
        game.generatePlayableBoard();
    }

    //VALIDATE_GAME
    public boolean isValidSolution() {
        return game.isValidSolution();
    }
    public boolean isComplete() {
        return game.isComplete();
    }

    // SELECT_CELL
    public void setValue(int row, int col, int value) {
        game.setValue(row, col, value);
    }

    // SURRENDER
    public void surrender() {
        game.completeGame();
    }

    // GET_BOARD
    public CellDTO[][] getBoardCurrent() {
        Cell[][] cells = game.getCells();
        int n = cells.length;
        CellDTO[][] currentCells = new CellDTO[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                currentCells[i][j] = new CellDTO(cells[i][j].getValueCurrent(), cells[i][j].isEditable());
            }
        }
        return currentCells;
    }
}