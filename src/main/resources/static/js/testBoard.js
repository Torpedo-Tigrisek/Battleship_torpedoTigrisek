document.getElementById("shipForm").addEventListener("submit", function (event) {
    event.preventDefault();
    var shipType = document.getElementById("shipType").value;
    var orientationCheckbox = document.getElementById("orientation");
    var isHorizontal = orientationCheckbox.value === "HORIZONTAL";

    let startX = parseInt(document.getElementById("startX").value, 10);
    let startY = parseInt(document.getElementById("startY").value, 10);

    placeShip(shipType, startX, startY, isHorizontal);
});

var stompClient = null;

function connect() {
    var socket = new SockJS('/battleship-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/shipPlaced', function (boardMessage) {
            var board = JSON.parse(boardMessage.body);
            updateBoard(board);
        });
    });
}

function placeShip(shipType, startX, startY, isHorizontal) {
    let coordinates = calculateCoordinatesForShip(shipType, startX, startY, isHorizontal);

    stompClient.send("/app/placeShip", {}, JSON.stringify({
        'shipType': shipType,
        'coordinates': coordinates,
        'orientation': isHorizontal
    }));
}

function calculateCoordinatesForShip(shipType, startX, startY, isHorizontal) {
    let coordinates = [];
    let shipSize = getShipSize(shipType);

    for (let i = 0; i < shipSize; i++) {
        if (isHorizontal) {
            coordinates.push({ x: startX + i, y: startY });
        } else {
            coordinates.push({ x: startX, y: startY + i });
        }
    }

    return coordinates;
}

function getShipSize(shipType) {
    switch (shipType) {
        case "CRUISER": return 4;
        case "SUBMARINE": return 3;
        case "DESTROYER": return 2;
        case "ATTACKER": return 1;
        default: return 0;
    }
}

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

document.addEventListener('DOMContentLoaded', function() {
    connect();
});


