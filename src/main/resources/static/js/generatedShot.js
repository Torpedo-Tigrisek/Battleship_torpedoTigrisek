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
            alert("Ezen a helyen már van egy X!");
            return;
        }

        var blueX = document.createElement("span");
        blueX.className = "blue-x";
        blueX.textContent = "X";

        // Find the cell with the specified ID
        var cell = document.getElementById(cellId);
    //    cell.className ="table-cell ship"

        cell.appendChild(blueX);

        generatedPositions.push(cellId);

    }, 2000); // késleltetés
}

function gettingRandomShotsFromServer() {
    stompClient.subscribe('/app/generatedShot', function (message) {
        console.log("EZ ITT BENYER KOORDINÁTA: " + message.body);
        placeBlueXAutomatically(message.body);
    });
}