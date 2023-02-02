let usernamePage = document.querySelector('#username-page');
let chatPage = document.querySelector('#chat-page');
let usernameForm = document.querySelector('#usernameForm');
let messageForm = document.querySelector('#messageForm');
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let connectingElement = document.querySelector('.connecting');


let stompClient = null;
let username = null;

let colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

//이전 채팅 기록을 불러오면서 userName도 같이 불러온다
userName = "test1";

//서버 연결
let socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
let header = {
    Authorization : "Bearer " + localStorage.getItem("accessToken")
};

stompClient.connect(header,onConnected,onError);

function onConnected() {
    stompClient.subscribe('/sub/chat/room/'+roomId, onMessageReceived);
    stompClient.send("/pub/chat/enter",
        header,
        JSON.stringify({
            'roomId':roomId,
            'userName':username,
            'chatType':'JOIN'
        }));
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    if(message.type === 'JOIN') {
        console.log("입장");
    } else if (message.type === 'LEAVE') {
        console.log("퇴장");
    } else {
        $('#messageArea').append(message.message);
    }

}

function onError() {

}

function sendMessage() {
    let messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        let chatMessage = {
            'roomId':roomId,
            userName: username,
            'message': messageInput.value,
            chatType: 'CHAT'
        };
        stompClient.send("/pub/chat/sendMessage", header, JSON.stringify(chatMessage));

        $('#messageArea').append(messageInput.value);

        messageInput.value = '';
    }
}

messageForm.addEventListener('submit', sendMessage, true)