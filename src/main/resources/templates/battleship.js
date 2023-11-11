// Dynamically create a 10x10 game board with draggable ships at (2,1) and (8,4)
var gameBoard = document.getElementById("game-board");

for (var i = 1; i <= 10; i++) {
    for (var j = 1; j <= 10; j++) {
        var cell = document.createElement("div");
        cell.className = "cell";
        cell.id = "cell-" + i + "-" + j;

        // Check if the current cell is at coordinates (2,1)
        if (i === 2 && j === 1) {
                var ship1 = createShip();
                cell.appendChild(ship1);
        }

        // Check if the current cell is at coordinates (8,4)
        if (i === 8 && j === 4) {
                var ship2 = createShipTwo();
                cell.appendChild(ship2);
        }
        gameBoard.appendChild(cell);
    }
}

// Add event listeners for drag and drop
var dragged;

gameBoard.addEventListener("dragstart", function (event) {
    dragged = event.target;
    event.dataTransfer.setData("text/plain", null);
});

gameBoard.addEventListener("dragover", function (event) {
    event.preventDefault();
});

gameBoard.addEventListener("drop", function (event) {
    event.preventDefault();
    // Ensure the drop target is a cell
    if (event.target.className === "cell") {
        // Get the coordinates of the cell
        var cellId = event.target.id;
        var coordinates = cellId.split("-").slice(1).map(Number);
        var row = coordinates[0];
        var col = coordinates[1];

        // Get the dimensions of the ship being dragged
        var shipWidth;
        if (dragged.className === "ship") {
            shipWidth = 1;
        } else if (dragged.className === "shipTwo") {
            shipWidth = 2;
        }
        // Check if the ship can be placed within the boundaries of the board
        if (col + shipWidth - 1 <= 10) {
            event.target.appendChild(dragged);
        }
    }
});

function createShip() {
    var ship = document.createElement("div");
    ship.className = "ship";
    // Make the ship draggable
    ship.draggable = true;
    return ship;
}

function createShipTwo() {
    var shipTwo = document.createElement("div");
    shipTwo.className = "shipTwo";
    //Make the ship draggable
    shipTwo.draggable = true;
    return shipTwo;
}