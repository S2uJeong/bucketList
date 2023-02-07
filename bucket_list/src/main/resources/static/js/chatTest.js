let usernamePage = document.querySelector('#username-page');
let chatPage = document.querySelector('#chat-page');
let usernameForm = document.querySelector('#usernameForm');
let messageForm = document.querySelector('#messageForm');
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let connectingElement = document.querySelector('.connecting');


let stompClient = null;

let colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

//토큰에서 memeberId와 userName을 꺼낸다
const userName = "test1";
const memebrId = "";

//서버 연결
let socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
let header = {
    Authorization : "Bearer " + localStorage.getItem("accessToken")
};

console.log("header" + header);

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


const Chat = (function(){
    const myName = "blue";

    // init 함수
    function init() {
        // enter 키 이벤트
        $(document).on('keydown', 'div.input-div textarea', function(e){
            if(e.keyCode == 13 && !e.shiftKey) {
                e.preventDefault();
                const message = $(this).val();

                // 메시지 전송
                sendMessage(message);
                // 입력창 clear
                clearTextarea();
            }
        });
    }

    // 메세지 태그 생성
    function createMessageTag(LR_className, senderName, message) {
        // 형식 가져오기
        let chatLi = $('div.chat.format ul li').clone();

        // 값 채우기
        chatLi.addClass(LR_className);
        chatLi.find('.sender span').text(senderName);
        chatLi.find('.message span').text(message);

        return chatLi;
    }

    // 메세지 태그 append
    function appendMessageTag(LR_className, senderName, message) {
        const chatLi = createMessageTag(LR_className, senderName, message);

        $('div.chat:not(.format) ul').append(chatLi);

        // 스크롤바 아래 고정
        $('div.chat').scrollTop($('div.chat').prop('scrollHeight'));
    }

    // 메세지 전송
    function sendMessage(message) {
        // 서버에 전송하는 코드로 후에 대체
        const data = {
            "senderName"    : "blue",
            "message"        : message
        };

        // 통신하는 기능이 없으므로 여기서 receive
        resive(data);
    }

    // 메세지 입력박스 내용 지우기
    function clearTextarea() {
        $('div.input-div textarea').val('');
    }

    // 메세지 수신
    function resive(data) {
        const LR = (data.senderName != myName)? "left" : "right";
        appendMessageTag("right", data.senderName, data.message);
    }

    return {
        'init': init
    };
})();

/*$(function(){
    Chat.init();
});*/