var placedPositions = [];
var stompClient = null;

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

    // Az "X" koordinátáit hozzáadjuk a listához, kinyerjük a két koordinátát
    placedPositions.push(cellId);
    var coordinates = cellId.split("-");
    var row = coordinates[2];
    var column = coordinates[3];
    var cellIdCoordinates =[ row + ", " +  column];
    sendXPositionsToServer(cellIdCoordinates);
}

function sendXPositionsToServer(cellIdCoordinates) {
    var shotCoordinates = {
        coordinates: cellIdCoordinates
    }
    stompClient.send(
        "/app/battle.sendShot",
        {},
        JSON.stringify(shotCoordinates)
    );
    console.log("Shot was sent to server:", shotCoordinates.coordinates);
    gettingRandomShotsFromServer();
}