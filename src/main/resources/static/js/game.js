var placedPositions = [];
var stompClient = null;

function connectToGame(event){
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public');
    });

}


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
    placedPositions.forEach(function (position) {
        var coordinates = position.split("-");
        var row = coordinates[1];
        var column = coordinates[2];
        console.log("Elhelyezett X a következő helyen: Sor: " + row + ", Oszlop: " + column);
        var shotCoordinates = {
            coordinates: placedPositions.value
        }
        stompClient.send(
            "/app/battle.sendShot",
            {},
            JSON.stringify(shotCoordinates)
        );
        console.log("Board update message:", shotCoordinates);
    });
}
