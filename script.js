const COLS = 10;
const ROWS = 20;
const TETROMINOS = {
    I: [[1, 1, 1, 1]],
    O: [[1, 1], [1, 1]],
    T: [[0, 1, 0], [1, 1, 1]],
    S: [[0, 1, 1], [1, 1, 0]],
    Z: [[1, 1, 0], [0, 1, 1]],
    J: [[1, 0, 0], [1, 1, 1]],
    L: [[0, 0, 1], [1, 1, 1]]
};
let board = Array.from({ length: ROWS }, () => Array(COLS).fill(0));
let currentPiece;
let nextPiece;
let score = 0;
let level = 1;
let lines = 0;
let gameOver = false;

function drawBoard() {
    const gameArea = document.getElementById("game-area");
    gameArea.innerHTML = "";
    board.forEach(row => {
        row.forEach(cell => {
            const cellDiv = document.createElement("div");
            if (cell) {
                cellDiv.className = "tetromino-" + cell;
            }
            gameArea.appendChild(cellDiv);
        });
    });
}

function spawnPiece() {
    const keys = Object.keys(TETROMINOS);
    const randomKey = keys[Math.floor(Math.random() * keys.length)];
    currentPiece = { shape: TETROMINOS[randomKey], x: 3, y: 0, type: randomKey };
    if (!nextPiece) {
        nextPiece = { shape: TETROMINOS[randomKey], type: randomKey };
    }
}

function movePiece(dx, dy) {
    currentPiece.x += dx;
    currentPiece.y += dy;
    drawBoard();
}

function rotatePiece() {
    const shape = currentPiece.shape;
    currentPiece.shape = shape[0].map((val, index) => shape.map(row => row[index]).reverse());
    drawBoard();
}

function dropPiece() {
    movePiece(0, 1);
}

function checkCollision() {
    // Collision detection logic
}

function clearLines() {
    // Line clearing logic
}

function updateScore() {
    document.getElementById("score").innerText = score;
    document.getElementById("level").innerText = level;
    document.getElementById("lines").innerText = lines;
}

function gameLoop() {
    if (!gameOver) {
        dropPiece();
        clearLines();
        updateScore();
        setTimeout(gameLoop, 1000 / level);
    }
}

document.addEventListener("keydown", event => {
    if (event.key === "ArrowLeft") movePiece(-1, 0);
    if (event.key === "ArrowRight") movePiece(1, 0);
    if (event.key === "ArrowDown") dropPiece();
    if (event.key === "ArrowUp" || event.key === " ") rotatePiece();
});

spawnPiece();
drawBoard();
gameLoop();
