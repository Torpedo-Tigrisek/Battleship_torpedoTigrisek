var placedPositions = [];
var stompClient = null;
var messages = document.querySelector('#messages');

function connectToGame(){
    var socket = new SockJS('/battleship-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public');
    });

}
connectToGame();

function placeX(cell) {
    var cellId = cell.id;

    // Ellenőrizzük, hogy az adott helyre már lett-e "X" elhelyezve
    if (placedPositions.includes(cellId)) {
        alert("Ezen a helyen már van egy X!");
        return;
    }

    var redX = document.createElement("span");
    redX.className = "red-x";
    redX.textContent = "X";
    cell.appendChild(redX);

    // Az "X" helyét hozzáadjuk a listához
    placedPositions.push(cellId);
    sendXPositionsToServer();
}

function sendXPositionsToServer() {
    //  placedPositions.forEach(function (position) {
    //      var coordinates = position.split("-");
    //  });

    var coordinates = placedPositions.at(placedPositions.length-1).split("-");
    var row = coordinates[0];
    var column = coordinates[1];
    console.log("Elhelyezett X a következő helyen: Sor: " + row + ", Oszlop: " + column);

    var shotCoordinates = {
        coordinates: coordinates
    }
    stompClient.send(
        "/app/battle.sendShot",
        {},
        JSON.stringify(shotCoordinates)
    );
    console.log("Shot was sent to server:", shotCoordinates.coordinates);
    reply();
}
function reply(){
    stompClient.subscribe('/app/reply', function(message) {
        messages.append(message.body);
    });
}
