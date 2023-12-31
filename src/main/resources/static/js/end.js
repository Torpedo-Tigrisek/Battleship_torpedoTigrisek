let popup = document.getElementById("popup");
const popupMessage = document.getElementById("popup-message");
var playerHit = 0;
var enemyHit = 0;
var endMessageSent = false;

function isEnd(){
    if (isGameEnd()) {
        if(!endMessageSent){
            endMessageSent = true;
            getEndMessage();
        }
    }
}
function isGameEnd() {
    return playerHit === 20 || enemyHit === 20;
}
function getEndMessage() {
    stompClient.subscribe('/app/end', function (message) {
        console.log("A játék végekor ezt küldi a szerver: " + message.body);
        popupMessage.innerHTML = message.body;
        openPopup();
    });
}

function openPopup() {
    popup.classList.add("open-popup");
}
