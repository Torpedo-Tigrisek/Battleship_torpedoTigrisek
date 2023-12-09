var placedPositions = [];
var placedHitPositions = [];
var generatedPositions = [];
var stompClient = null;
var canPlaceShips = true;
var shipsPlaced = false;
let isPlayerTurn = true;
let isEnemyTurn = false;

function connectToGame() {
    var socket = new SockJS('/battleship-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public');
    });
}

connectToGame();

function playHitSound() {
    var audioHit = document.getElementById('hitSound');
    audioHit.play();
}
function playWaterDropSound() {
    var waterDropSound = document.getElementById('waterDropSound');
    waterDropSound.play();
}
document.getElementById('placeShip').addEventListener('click', function() {
    shipsPlaced = true;
    var playGameButton = document.getElementById('playGame');
    playGameButton.disabled = false;

    playGameButton.addEventListener('click', function() {
        playGameButton.disabled = true;
    }, { once: true });
});

function placeX(cell) {
    if (!isPlayerTurn) {
        alert("It's not your turn!");
        return;
    }
    var cellId = cell.id;
    if (canPlaceShips) {
        alert("Place your ships first and then press the Start button!");
        return;
    }
    if (!shipsPlaced) {
        alert("Place your ships first!");
        return;
    }

    if (placedPositions.includes(cellId)) {
        alert("You've already shot here!");
        return;
    }

    var redX = document.createElement("span");
    redX.className = "red-x";
    redX.textContent = "X";
    cell.appendChild(redX);

    placedPositions.push(cellId);
    var coordinates = cellId.split("-");
    var row = coordinates[2];
    var column = coordinates[3];
    var cellIdCoordinates = [row + ", " + column];
    sendXPositionsToServer(cellIdCoordinates);

    if (cell.textContent.includes("S")) {
        console.log("Hit detected!");
        cell.style.backgroundColor = "red";
        playHitSound();

        placedHitPositions.push(cellId);
        var hitCoordinates = cellId.split("-");
        var hitRow = hitCoordinates[2];
        var hitColumn = hitCoordinates[3];
        var cellIdHitCoordinates = [hitRow + ", " +hitColumn];
        sendHitPositionsToServer(cellIdHitCoordinates);
        playerHit++;
        isEnd();
        console.log('PLAYER HIT ' + playerHit + ' : ' + enemyHit + ' ENEMY HIT' )
        isPlayerTurn = true;
        isEnemyTurn = false;
    } else {
        playWaterDropSound();
        isPlayerTurn = false;
        isEnemyTurn = true;
        gettingRandomShotsFromServer();
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
    }

    function sendHitPositionsToServer(cellIdHitCoordinates) {
        var hitCoordinates = {
            hitCoordinates: cellIdHitCoordinates
        }
        stompClient.send(
            "/app/battle.sendHit",
            {},
            JSON.stringify(hitCoordinates)
        );
        console.log("Hit was sent to server:", hitCoordinates.coordinates);
    }

    function placeBlueXAutomatically(message) {
        if (!isEnemyTurn) {
            return;
        }
        setTimeout(function () {
            var convertedMessageObject = JSON.parse(message);
            var randomRow = convertedMessageObject.coordinates[0];
            var randomColumn = convertedMessageObject.coordinates[1];
            var cellId ='cell-' + randomRow + '-' + randomColumn;

            if (generatedPositions.includes(cellId)) {
                gettingRandomShotsFromServer();
                return;
            }

            var blueX = document.createElement("span");
            blueX.className = "blue-x";
            blueX.textContent = "X";

            var cell = document.getElementById(cellId);
            cell.appendChild(blueX);

            if (cell.textContent.includes("S")) {
                cell.style.backgroundColor = "red";
                playHitSound();
                enemyHit++;
                console.log('PLAYER HIT ' + playerHit + ' : ' + enemyHit + ' ENEMY HIT' );
                isEnd();
                isEnemyTurn = true;
                isPlayerTurn = false;
                gettingRandomShotsFromServer();
            } else {
                playWaterDropSound();
                isEnemyTurn = false;
                isPlayerTurn = true;
            }
            generatedPositions.push(cellId);
        }, 800);

        function playWaterDropSound() {
            var waterDropSound = document.getElementById('waterDropSound');
            waterDropSound.play();
        }
    }

    function gettingRandomShotsFromServer() {
        stompClient.subscribe('/app/generatedShot', function (message) {
            if (isEnemyTurn) {
                console.log("HERE ARE THE COORDINATES: " + message.body);
                placeBlueXAutomatically(message.body);
            }
        });
    }
}