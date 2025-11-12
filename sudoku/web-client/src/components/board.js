import createCell from "./cell.js";
import { setGameCell } from "../services/boardService.js";

const onSetValue = (cellInfo) => {
    const [row, column] = cellInfo.id.split('-').map(Number);
    const value = Number(cellInfo.val);
    setGameCell(row, column, value).then(response => {
        console.log('Cell updated on server:', response);
    }).catch(error => {
        console.error('Error updating cell on server:', error);
    });
}

const createBoard = (dataBoard) => {
    let n = 3;
    let m = 3;
    const board = document.createElement('div');
    board.classList.add('board');
    for (let i = 0; i < n; i++) {
        const row = document.createElement('div');
        row.classList.add('row');
        for (let j = 0; j < m; j++) {
            const cell = document.createElement('div');
            cell.classList.add('cell');
            for (let k = 0; k < n; k++) {
                const subRow = document.createElement('div');
                subRow.classList.add('subRow');
                cell.appendChild(subRow);
                for (let l = 0; l < m; l++) {
                    const subCell = document.createElement('div');
                    subCell.classList.add('subCell');
                    subCell.appendChild(createCell(
                        {...dataBoard[i * n + k][j * m + l], id: `${i * n + k}-${j * m + l}`, onSetValue}
                    ));
                    subRow.appendChild(subCell);
                }
            }
            row.appendChild(cell);
        }
        board.appendChild(row);
    }
    return board;
}

export default createBoard;
