import axios from 'axios';

async function getGameData() {
    const response = await axios.get('http://localhost:3001/board');
    return response.data;
}

async function setGameCell(row, column, value) {
    const response = await axios.put(`http://localhost:3001/board`, {
        row,
        column,
        value
    });
    return response.data;
}

async function resetGameBoard() {
    const response = await axios.post(`http://localhost:3001/board`);
    return response.data;
}

async function solveGameBoard() {
    const response = await axios.put(`http://localhost:3001/board/solution`);
    return response.data;
}

async function validateGameBoard() {
    const response = await axios.get(`http://localhost:3001/board/solution`);
    return response.data;
}

export { getGameData, setGameCell, resetGameBoard, solveGameBoard, validateGameBoard };