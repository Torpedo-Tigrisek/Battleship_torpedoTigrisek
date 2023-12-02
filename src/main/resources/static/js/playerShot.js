var placedPositions = [];
var placedHitPositions = [];
var generatedPositions = [];
var stompClient = null;
var canPlaceShips = true;

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
    } else {
        // Ha a találat nem hajóra esett, játssza le a vízcsepp hangot
        playWaterDropSound();
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
}


