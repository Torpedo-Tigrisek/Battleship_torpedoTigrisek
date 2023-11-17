document.getElementById("shipForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Megakadályozza az űrlap alapértelmezett beküldési viselkedését
    var shipType = document.getElementById("shipType").value;
    var startX = document.getElementById("startX").value;
    var startY = document.getElementById("startY").value;
    var orientation = document.getElementById("orientation").value;

    placeShip(shipType, parseInt(startX, 10), parseInt(startY, 10), orientation);
});

var stompClient = null;

function connect() {
    var socket = new SockJS('/battleship-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/shipPlaced', function (boardMessage) {
            console.log("Board update message:", boardMessage);
            var board = JSON.parse(boardMessage.body);
            updateBoard(board);
        });
    });
}

function placeShip(shipType, startX, startY, orientation) {
    stompClient.send("/app/placeShip", {}, JSON.stringify({
        'shipType': shipType,
        'startX': startX,
        'startY': startY,
        'orientation': orientation
    }));
}

// Hívja meg a connect funkciót az oldal betöltésekor
connect();

function updateBoard(board) {
    board.grid.forEach((row, rowIndex) => {
        row.forEach((cell, colIndex) => {
            var cellId = `cell-${rowIndex}-${colIndex}`;
            var cellElement = document.getElementById(cellId);
            if (cellElement) {
                cellElement.textContent = cell;
                cellElement.className = cell === 'S' ? 'table-cell ship' : 'table-cell';
            }
        });
    });
}

// Egy funkció, amely hozzáadja a kattintás eseménykezelőket a tábla minden cellájához.
function addClickHandlersToCells() {
    var cells = document.querySelectorAll('.table-cell'); // Az összes cella kiválasztása.
    cells.forEach(function(cell) {
        cell.addEventListener('click', function() {
            // Ellenőrzi, hogy a cella üres-e (nem tartalmaz 'S'-t vagy 'X'-et).
            if (cell.textContent === '~') {
                cell.textContent = 'X'; // Ha üres, akkor beír egy 'X'-et.
            }
        });
    });
}

// Meghívja az addClickHandlersToCells funkciót az oldal betöltésekor.
document.addEventListener('DOMContentLoaded', addClickHandlersToCells);

cell.addEventListener('click', function() {
    if (cell.textContent === '~') {
        // Itt küldj WebSocket üzenetet a szervernek
        var cellId = cell.getAttribute('id');
        var [_, rowIndex, colIndex] = cellId.split('-');

        stompClient.send("/app/updateCell", {}, JSON.stringify({
            rowIndex: parseInt(rowIndex),
            colIndex: parseInt(colIndex),
            newValue: 'X'
        }));
    }
});