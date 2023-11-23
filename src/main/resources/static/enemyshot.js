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
    gettingRandomShotsFromServer();
}

function placeBlueXAutomatically(message) {
    // Simulate placing a blue X automatically after a short delay (e.g., 2 seconds)
    setTimeout(function () {
        // Assuming the grid has cells with IDs like "1-1", "1-2", ..., "10-10"
        console.log("funkcóba rakott message:_____________ " + message)
        var convertedMessageObject = JSON.parse(message);
        var randomRow = convertedMessageObject.coordinates[0];
        var randomColumn = convertedMessageObject.coordinates[1];
        console.log(randomRow + " and " +randomColumn);
        var cellId = randomRow + '-' + randomColumn;

        // Check if the position has already been placed
        if (placedPositions.includes(cellId)) {
            alert("Ezen a helyen már van egy X!");
            return;
        }

        var blueX = document.createElement("span");
        blueX.className = "blue-x";
        blueX.textContent = "X";

        // Find the cell with the specified ID
        var cell = document.getElementById(cellId);

        // Append the blue X to the cell
        cell.appendChild(blueX);

        // Add the position to the list
        placedPositions.push(cellId);

    }, 2000); // 2-second delay, adjust as needed
}

function gettingRandomShotsFromServer(){
    stompClient.subscribe('/app/generatedShot', function(message) {
        console.log("EZ ITT BENYER KOORDINÁTA: " + message.body);
        placeBlueXAutomatically(message.body);
    });
}
