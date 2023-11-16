document.getElementById("board-container").addEventListener("submit", function(event){
    event.preventDefault();
    var size = document.getElementById("size").value;
    var startX = document.getElementById("startX").value;
    var startY = document.getElementById("startY").value;
    placeShip(size, startX, startY);
});

var stompClient = null;

function connect() {
    var socket = new SockJS('/battleship-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/shipPlaced', function (ship) {
            // Itt frissítsd a táblát az új hajóval
        });
    });
}

function placeShip(size, startX, startY) {
    stompClient.send("/app/placeShip", {}, JSON.stringify({'size': size, 'startX': startX, 'startY': startY}));
}

// Hívja meg a connect funkciót az oldal betöltésekor
connect();

stompClient.subscribe('/topic/shipPlaced', function (shipMessage) {
    var ship = JSON.parse(shipMessage.body);
    addShipToBoard(ship);
});

function addShipToBoard(ship) {
    for (let i = 0; i < ship.size; i++) {
        var cell;
        if (ship.orientation === 'HORIZONTAL') {
            cell = document.querySelector(`#cell-${ship.startY}-${ship.startX + i}`);
        } else {
            cell = document.querySelector(`#cell-${ship.startY + i}-${ship.startX}`);
        }
        if (cell) {
            cell.classList.add('ship');
        }
    }
}

