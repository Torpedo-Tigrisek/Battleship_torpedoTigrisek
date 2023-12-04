var generatedPositions = [];
function placeBlueXAutomatically(message) {
    setTimeout(function () {
        //
        var convertedMessageObject = JSON.parse(message);
        var randomRow = convertedMessageObject.coordinates[0];
        var randomColumn = convertedMessageObject.coordinates[1];
        console.log('GENERATED VALUES: ' + randomRow + " and " +randomColumn);
        var cellId ='cell-' + randomRow + '-' + randomColumn;

        // Check if the position has already been placed
        if (generatedPositions.includes(cellId)) {
        //    alert("Ezen a helyen már van egy X!");
            gettingRandomShotsFromServer();
            return;
        }

        var blueX = document.createElement("span");
        blueX.className = "blue-x";
        blueX.textContent = "X";

        // Find the cell with the specified ID
        var cell = document.getElementById(cellId);
    //    cell.className ="table-cell ship"

        cell.appendChild(blueX);
        // Set the background color to red if it's a hit
        if (cell.textContent.includes("S")) {
            cell.style.backgroundColor = "red";
            playHitSound();
            enemyHit++;
            isEnd();
            console.log('PLAYER HIT ' + playerHit + ' : ' + enemyHit + ' ENEMY HIT' )
        } else {
            // Ha a találat nem hajóra esett, játssza le a vízcsepp hangot
            playWaterDropSound();
        }

        generatedPositions.push(cellId);

    }, 1000); // késleltetés
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
