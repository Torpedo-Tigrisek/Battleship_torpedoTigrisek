document.getElementById('startGameButton').addEventListener('click', function() {
    // Játékos táblájának inaktívvá tétele
    document.querySelectorAll('.player-board .table-cell').forEach(cell => {
        cell.style.pointerEvents = 'none';
    });

    // Ellenfél táblájának aktívvá tétele
    document.querySelectorAll('.opponent-board .opponent-table-cell').forEach(cell => {
        cell.style.pointerEvents = 'auto';
    });
});

// Bubu
var tabelCells= document.querySelectorAll('.opponent-table-cell');
 tabelCells.forEach(cell => {
     cell.addEventListener('click',function (ev){
         var xhttp = new XMLHttpRequest();
         xhttp.onreadystatechange = function() {
             if (this.readyState == 4 && this.status == 200) {
                 // Typical action to be performed when the document is ready:
                 var resp = JSON.parse(this.response)
                 console.log(resp)
             }
         };
         xhttp.open("GET", "getEnemyShipsMap", true);
         xhttp.send();
     });
 });