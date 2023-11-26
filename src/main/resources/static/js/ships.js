document.getElementById("placeShip").addEventListener("click", function() {
    placeRandomShips();
});

function placeRandomShips() {
    stompClient.send("/app/placeRandomShips", {}, {});
}

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
        stompClient.subscribe('/topic/remainingShips', function (message) {
            var remainingShips = JSON.parse(message.body);
            updateShipSelect(remainingShips);
        });
    });
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

function updateShipSelect(remainingShips) {
    var shipSelect = document.getElementById("shipType");
    shipSelect.innerHTML = ''; // Töröljük a korábbi opciókat

    remainingShips.forEach(shipType => {
        var option = document.createElement("option");
        option.value = shipType;
        option.textContent = shipType.charAt(0).toUpperCase() + shipType.slice(1).toLowerCase(); // Formázott megjelenítés
        shipSelect.appendChild(option);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    connect();
});
