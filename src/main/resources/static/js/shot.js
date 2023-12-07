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
        alert("Nem a te köröd van!");
        return;
    }
    var cellId = cell.id;
    if (canPlaceShips) {
        alert("Előbb helyezd el a hajókat majd nyomj a Start gombra!");
        return;
    }
    if (!shipsPlaced) {
        alert("Előbb helyezd le a hajókat!");
        return;
    }

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
    var cellIdCoordinates = [row + ", " + column];
    sendXPositionsToServer(cellIdCoordinates);

    // Ellenőrizzük, hogy az adott helyen van-e hajó ("S")
    if (cell.textContent.includes("S")) {
        // Találat esetén pirosra változtatjuk a cella színét
        console.log("Hit detected!");
        cell.style.backgroundColor = "red";
        playHitSound();

        // Hozzáadjuk a találat koordinátáit a "HitCoordinate" listához
        placedHitPositions.push(cellId);
        var hitCoordinates = cellId.split("-");
        var hitRow = hitCoordinates[2];
        var hitColumn = hitCoordinates[3];
        var cellIdHitCoordinates = [hitRow + ", " +hitColumn];
        sendHitPositionsToServer(cellIdHitCoordinates);
        playerHit++;
        isEnd();
        console.log('PLAYER HIT ' + playerHit + ' : ' + enemyHit + ' ENEMY HIT' )
        isPlayerTurn = true; // Ezt beállítjuk, ha a játékos talált
        isEnemyTurn = false;
    } else {
        // Ha a találat nem hajóra esett, játssza le a vízcsepp hangot
        playWaterDropSound();
        isPlayerTurn = false; // Ezzel jelezzük, hogy az ellenfél jön
        isEnemyTurn = true;
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
                isEnd();
                console.log('PLAYER HIT ' + playerHit + ' : ' + enemyHit + ' ENEMY HIT' );
                // Ha az ellenfél talált, marad az ő köre
                isEnemyTurn = true;
                isPlayerTurn = false;
                // Itt visszatérünk, hogy újabb lövést végezzen az ellenfél
                gettingRandomShotsFromServer();
            } else {
                playWaterDropSound();
                // Ha az ellenfél nem talált, akkor az ő köre véget ért
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
            console.log("EZ ITT BENYER KOORDINÁTA: " + message.body);
            placeBlueXAutomatically(message.body);
        });
    }
}