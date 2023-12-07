document.getElementById("placeShip").addEventListener("click", function() {
    if (canPlaceShips) {
        placeRandomShips();
    } else {
        alert("Game has already started. No more ship placements allowed.");
    }
});

document.getElementById("playGame").addEventListener("click", startGame);

function startGame() {
    canPlaceShips = false;

    stompClient.send("/app/ready", {}, {});
    alert("Game has started. No more ship placements allowed.");
}



function placeRandomShips() {
    stompClient.send("/app/placeRandomShips", {}, {});
}
function placeEnemyShips() {
    stompClient.send("/app/placeEnemyShips", {}, {});
}
function gettingRandomShotsFromServer() {
    stompClient.subscribe('/app/generatedShot', function (message) {
        console.log("HERE ARE THE COORDINATES: " + message.body);
        placeBlueXAutomatically(message.body);
    });
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
        stompClient.subscribe('/topic/shipData', function (shipDataMessage) {
            var shipData = JSON.parse(shipDataMessage.body);
            handleShipData(shipData);
        });
    });
}

function handleShipData(shipData) {

    console.log("Received ship data:", shipData);

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