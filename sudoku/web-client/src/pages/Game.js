
import createBoard from '../components/board.js';
import axios from 'axios';
import { getGameData, resetGameBoard, solveGameBoard, validateGameBoard } from '../services/boardService.js';

import DelegateIce from '../services/delegateIce.js';

import counter from '../components/counter.js';

let cellData = [];

async function getUserData() {
    const registros = await fetch('http://localhost:3001/register', { method: 'GET' });
    const data = await registros.json();
    return data;
}

async function getPokemonData() {
    const response = await axios.get('https://pokeapi.co/api/v2/pokemon/ditto');
    console.log(response.data);
    return response;
}

function Game() {

    validateUser();
    const usuario = localStorage.getItem('usuario');
    const delegateIce = new DelegateIce(usuario);

    delegateIce.printMessage('Usuario accedió a la página del juego Sudoku').then(() => {
        console.log('Mensaje enviado al servidor Ice.');
    });

    const container = document.createElement('div');
    container.className = 'gameContainer';

    const heading = document.createElement('h1');

    const counterComponent = counter({ delegateIce: delegateIce });

    const actions = document.createElement('div');
    actions.className = 'gameActions';

    const resetButton = document.createElement('button');
    resetButton.textContent = 'Reiniciar';
    resetButton.onclick = () => {
        resetGameBoard().then(response => {
            printBoard(response.data.board);
        }).catch(error => {
            console.error('Error reiniciando el juego:', error);
        });
    };
    const validateGameButton = document.createElement('button');
    validateGameButton.textContent = 'Validar';
    validateGameButton.onclick = () => {
        validateGameBoard().then(response => {
            console.log('Respuesta de validación:', response);
            if (response.data.win) {
                playWinCascade();
            } else {
                alert('El Sudoku no está completo o tiene errores. Por favor, revisa tu solución.');
            }
        }).catch(error => {
            console.error('Error resolviendo el juego:', error);
        });
    };

    const surrenderButton = document.createElement('button');
    surrenderButton.textContent = 'Rendir';
    surrenderButton.onclick = () => {
        solveGameBoard().then(response => {
            printBoard(response.data.board);
            alert('Has decidido rendirte. El tablero se ha completado automáticamente.');
        }).catch(error => {
            console.error('Error rindiéndose en el juego:', error);
        });
    };

    actions.appendChild(validateGameButton);
    actions.appendChild(resetButton);
    actions.appendChild(surrenderButton);

    const footer = document.createElement('footer');
    footer.id = 'footer';
    footer.className = 'gameFooter';
    heading.textContent = 'Bienvenido al Sudoku';

    const board = document.createElement('div');
    board.className = 'board';

    container.appendChild(heading);
    container.appendChild(counterComponent);
    container.appendChild(actions);
    container.appendChild(board);
    container.appendChild(footer);

    getPokemonData().then(response => {
        const footer = document.getElementById('footer');
        if (footer) {
            footer.innerHTML = `
            <p>Test PokeAPI</p>
            <img src="${response.data.sprites.front_default}" alt="${response.data.name}" />
            <p>Nombre del pokemon: ${response.data.name}</p>`;
        }
    });

    getGameData().then(response => {
        cellData = response.data.board;
        printBoard(response.data.board);
    });

    return container;
}

function validateUser() {
    let usuario = localStorage.getItem('usuario');
    if (!usuario) {
        let username = '';
        while (!username) {
            username = prompt('Por favor, introduce tu nombre de usuario para iniciar sesión:');
            if (username === null) {
                alert('No has introducido un nombre de usuario. Por favor, recarga la página para intentarlo de nuevo.');
                return document.createElement('div');
            }
            username = username.trim();
        }
        localStorage.setItem('usuario', username);
        alert(`Bienvenido, ${username}! Has iniciado sesión correctamente.`);
        usuario = username;
    }
}

function printBoard(boardData) {
    const board = document.querySelector('.board');
    board.innerHTML = '';
    const newBoard = createBoard(boardData);
    board.replaceWith(newBoard);
}

function playWinCascade() {
    for (let i = 0; i < cellData.length; i++) {
        setTimeout(() => {
            for (let j = 0; j < cellData[i].length; j++) {
                const cellId = `${i}-${j}`;
                const cell = document.getElementById(cellId);
                if (cell) {
                    cell.classList.add('win-cell');
                    setTimeout(() => {
                        cell.classList.remove('win-cell');
                    }, 1000);
                }
            }
        }, i * 100);
    }
}

export default Game;