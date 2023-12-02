let popup = document.getElementById("popup");
var popupMessage = document.getElementById("popup-message");
var playerHit = 0;
var enemyHit = 0;

function isEnd(){
    if(playerHit === 20 || enemyHit === 20){
        getEndMessage();
    }

}
function getEndMessage() {
    stompClient.subscribe('/app/end', function (message) {
        console.log("A játék végekor ezt küldi a szerver: " + message.body);
        openPopup(message.body);
    });
}

function openPopup(message) {
    console.log(message)
    popupMessage = message;
    popup.classList.add("open-popup");
}
