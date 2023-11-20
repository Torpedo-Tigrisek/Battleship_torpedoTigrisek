'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

// ez a websocket
var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// it will allow to connect the user
function connect(event) {
    username = document.querySelector('#name').value.trim(); // trim >> hogy leszedje a space-eket
    if(username) {
        usernamePage.classList.add('hidden'); // ez rejti el a hidden rész >> a css file-ban is van erre parancs
        chatPage.classList.remove('hidden'); // a hidden részt remove-oljuk, ezért ezzel megjelenítődik ez a rész

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }

    event.preventDefault(); // a form eredeti működését akadájozzuk meg, hogy helyette ezeken a js parancsokon menjen végig

}
function onConnected() {

    //subscribe to the public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // tell username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
    // miután a user connect-tál, azután eltüntetjük ezt a részét az oldalnak
    connectingElement.classList.add('hidden');
}

function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page and try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    // a messageContent az az infó, amit beír a kliens = messageInput mínusz space-ek
    // majd ellenőrizzük, hogy a messageContent-be van-e valami és a stompClient aktív - a kapcsolat megvan mmég
    if(messageContent && stompClient) {
        //létrehozzuk a controllerbe is bevitt változó megfelelőjét és egyenlővé tesszük a cliens oldalról fogadott információkkal
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        // elküldjük a websocketnek az adatot
        stompClient.send(
            "/app/chat.sendMessage",
            {},
            JSON.stringify(chatMessage)
        );
        // újra üressé állítjuk a bevívendő adatnak ezt a változót
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {

    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    // showing the latest message
    messageArea.scrollTop = messageArea.scrollHeight;

}


// rendom avatár szín és formázás kiválasztás
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

// amikor a submitra kattint >> connect metódust meghívjuk
usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
