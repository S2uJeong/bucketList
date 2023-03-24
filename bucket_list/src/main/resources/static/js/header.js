// sns 로그인 accessToken 저장
const url = new URL(window.location.href)
const accessToken = url.searchParams.get('accessToken')

if(accessToken != null) {
    localStorage.setItem('accessToken', accessToken)
    window.location.href = '/';
}

// 로그인 상태 유지
window.onload = function () {
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken === null) {
        $("#login_li").show();
        $(".alarm-btn-box-li").hide();
        $(".chat-room-box-li").hide();
        $("#user_li").hide();
    } else {
        $("#login_li").hide();
        $(".alarm-btn-box-li").show();
        $(".chat-room-box-li").show();
        $("#user_li").show();

        const payload = accessToken.split('.')[1];
        let userName = JSON.parse(decodeURIComponent(escape(window.atob(payload)))).userName;
        document.getElementById("userName").innerText = userName;

        let userId = JSON.parse(decodeURIComponent(escape(window.atob(payload)))).memberId;
        $('#profileHref').attr('href', "/profile/" + userId);
    }

};

let code;
const config = {"Content-Type" : `application/json` };

//닉네임 중복 검사
async function checkUserName() {
    let userName = document.getElementById('form_userName').value;

    try {
        const response = await axios.post(
            '/api/v1/signup/validation/nickname',
            {'userName' : userName} ,
            config);
        if (response.status === 200) {
            document.getElementById('error_userName').style.display = 'none';
            document.getElementById('able_userName').style.display = 'block';
            document.getElementById('form_userName').readOnly = true;
        }
    } catch (error) {
        document.getElementById('error_userName').style.display = 'block';
        document.getElementById('able_userName').style.display = 'none';
    }
}

//이메일 인증
async function checkEmail() {
    let email = document.getElementById('form_email').value;

    try {
        const response = await axios.post(
            '/api/v1/signup/validation/mail',
            {'email' : email} ,
            config);

        code = response.data.result;
        document.getElementById('error_email').style.display = 'none';
        document.getElementById('send_email').style.display = 'block';

    } catch (error) {
        document.getElementById('error_email').style.display = 'block';
        document.getElementById('send_email').style.display = 'none';

    }
}

//비밀번호-비밀번호확인 일치여부
function checkPasswordCorrect() {
    let password_correct = document.getElementById('check_pw').value;
    let password = document.getElementById('pw').value;

    if (password_correct !== password) {
        document.getElementById('error_passwordCorrect').style.display = 'block';
    } else {
        document.getElementById('error_passwordCorrect').style.display = 'none';
    }
}

//회원가입 요청
async function join() {
    let check_userName = document.getElementById("form_userName").readOnly
    let userName = document.getElementById("form_userName").value;
    let pw = document.getElementById("pw").value;
    let check_pw = document.getElementById("check_pw").value;
    let gender = document.getElementById("form_gender").value;
    let age = document.getElementById("age").value;
    let email = document.getElementById("form_email").value;
    let input_code = document.getElementById("code").value;

    let data = {
        'userName' : userName,
        'email' : email,
        'password' : pw,
        'passwordCorrect' : check_pw,
        'age' : age,
        'gender' : gender
    }

    if (code !== input_code) {
        document.getElementById('error_code').style.display = 'block';
    }

    if (gender.length === 0) {
        document.getElementById('error_gender').style.display = 'block';
    }

    if(check_userName && (code === input_code) && gender.length>0) {
        document.getElementById('error_code').style.display = 'none';
        try {
            const res = await axios.post('/api/v1/signup', data, config);

            alert('회원가입 완료')
            window.location.href = '/';
        } catch (e) {
        }
    }
}

function logout() {
    localStorage.clear();
    window.location.href = '/';
}

async function login() {
    let email = document.getElementById('email').value;
    let pw = document.getElementById('password').value;

    try {
        data = {"email" : email, "password" : pw }
        const login_response = await axios.post('/api/v1/login', data, config);
        localStorage.setItem("accessToken", login_response.data.result.accessToken);
        window.location.reload();
    } catch (e) {
        const login_error_message = e.response.data.result.message;
        document.getElementById("login_error").style.display = 'block';
        document.getElementById("login_error").innerText = login_error_message;
    }
}

axios.interceptors.request.use(function(config) {
    const accessToken = localStorage.getItem('accessToken');
    if(accessToken) {
        config.headers.Authorization = 'Bearer' + ' ' + accessToken
    }
    return config;
})

axios.interceptors.response.use(
    success => success,
    async(error) => {
        const status = error.response.status

        if(status === 401) {
            const originRequest = error.config

            await axios.post('/api/v1/login/reissue')
                .then(response => {
                    localStorage.setItem("accessToken", response.data);
                    originRequest.headers.authorization = 'Bearer ' + response.data;
                    return axios(originRequest);
                })
                .catch(error => {
                    localStorage.removeItem('accessToken')
                    window.location.href = '/';
                })
            return Promise.reject(error)
        }
        return Promise.reject(error);
    }
)


//알람 관련
let alarmCnt = 1;
let checkAlarmCnt = 0;
let alarmArr = ['님이 회원님의 게시글에 댓글을 남겼습니다.  ','님이 회원님의 게시글을 좋아합니다.  ','님이 신청서를 작성하셨습니다.  ','님이 회원님의 신청서를 승낙하셨습니다.  ','님의 리뷰를 작성해주세요.  ',' 버킷리스트의 리뷰를 작성해주세요.  ','기타'];

function getAlarmList() {
    let html, i;
    let data;

    if(alarmCnt === 1 || checkAlarmCnt === 1) {
        axios({
            method:"GET",
            url: '/api/v1/alarms',
        }).then((res) => {
            data = res.data.result.content;
            if(data.length == 0) {
                if(checkAlarmCnt !== 1)
                    html += `<div class="text-secondary alarm-none">알림이 없습니다</div>`
            } else {
                for (i in data) {
                    html += alarmHtml(data[i]);
                }
            }
            $("#alarm-list").append(html.replace('undefined', ''));
        }).catch((error) => {

        });
        alarmCnt++;
    } else if(alarmCnt > 1) {
        let firstIdText = ($('.alarm-text').first()).attr('id');

        if(firstIdText == undefined) {
            checkAlarmCnt = 1;
            return;
        }

        let firstIdArr = firstIdText.split('-');
        let firstId = firstIdArr[1];
        let newHtml, j;

        axios({
            method:"GET",
            url: '/api/v1/alarms/new',
            params:{'id':firstId}
        }).then((newRes) => {
            data = newRes.data.result.content;
            if(data.length == 0) {
               $('.alarm-none').css('display','block');
            } else {
                $('.alarm-none').css('display','none');
                for (j in data) {
                    newHtml += alarmHtml(data[j]);
                }
            }
            $("#alarm-list").prepend(newHtml.replace('undefined',''));
        }).catch((error) => {

        });
    }
}

function getHoursMinTime(time) {
    let date = new Date(time);
    /*let nowDate = new Date();

    let dateDif = Math.abs((nowDate.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
    if(dateDif == 0) {*/
        let hours = ('0' + date.getHours()).slice(-2);
        let minutes = ('0' + date.getMinutes()).slice(-2);
        return hours+':'+minutes;
    /*} else if(dateDif > 0) {
        let month = ('0' + (date.getMonth() + 1)).slice(-2);
        let day = ('0' + date.getDate()).slice(-2);
        return month+'/'+day;
    }*/
}

//스크롤
$("#alarm-list").scroll(function (){
    let scrollTop = $(this).scrollTop();
    let innerHeight = $(this).innerHeight();
    let scrollHeight = $(this).prop('scrollHeight');

    if(scrollTop + innerHeight >= scrollHeight) {
        newScrollAlarm();
    }
});

//스크롤시 새로운 알람 가져오기
function newScrollAlarm() {
    let lastIdText = ($('.alarm-text').last()).attr('id');
    let lastIdArr = lastIdText.split('-');
    let lastId = lastIdArr[1];
    let html, i;
    let data;

    if(lastId == 1) return;

    axios({
        method:"GET",
        url: '/api/v1/alarms/new/scroll',
        params:{'id':lastId}
    }).then((res) => {
        data = res.data.result.content;
        if(data.length == 0) return;
        for(i=0; i<data.length; i++) {
            html += alarmHtml(data[i]);
        }
        $("#alarm-list").append(html.replace('undefined',''));
    }).catch((error) => {

    });
}

//알람 html 메서드
function alarmHtml(data) {
    let text = '';
    let modal = '';
    let onclick = '';

    if(data.category == 4) {
        modal = `data-bs-toggle="modal" data-bs-target="#memberReview"`;
        text = data.senderName+''+alarmArr[data.category];
        onclick = `onclick="memberReviewAlarm('${data.senderName}',${data.id})"`
    }
    else if(data.category == 5) {
        text = data.postTitle+''+alarmArr[data.category];
        modal = `data-bs-toggle="modal" data-bs-target="#bucketReview"`;
        onclick = `onclick="postReviewAlarm('${data.postTitle}',${data.postId},${data.id})"`
    } else {
        onclick = `onclick="readAlarm(${data.id},${data.postId})"`
        text = data.senderName+''+alarmArr[data.category];
    }
    return `<a href="javascript:void(0)" class="list-group-item list-group-item-action alarm-text" id="alarm-${data.id}" ${modal} ${onclick}>
                            ${text}
                            <small class="opacity-50 text-nowrap alarm-time">${getHoursMinTime(data.createdAt)}</small>
                            </a>`;
}

// 리뷰 modal 데이터 대체
function memberReviewAlarm(senderName, alarmId) {
    $('#memberReview_id').text(senderName);
    $('#memberReview_alarmId').val(alarmId);
}

function postReviewAlarm(postTitle, postId, alarmId) {
    $('#bucketReview_id').text(postId);
    $('#bucketReview_name').text(postTitle);
    $('#bucketReview_alarmId').val(alarmId);
}

//알람 하나 읽기
function readAlarm(id,postId) {
    let data;

    axios({
        method:"POST",
        url: '/api/v1/alarms',
        params: {'alarmId':id}
    }).then((res) => {
        data = res.data.result;
        if(data >= 1) {
            location.href='/post/'+postId;
        }
    }).catch((error) => {

    });
}

//알람 모두 읽기
function readAllAlarm() {
    let data;

    axios({
        method:"POST",
        url: '/api/v1/alarms/all',
    }).then((res) => {
        data = res.data.result;
        if(data >= 1) {
            alert("모두 읽음 처리 되었습니다.");
            /*$('#alarm-list').children().remove();
            let html = `<div class="text-secondary alarm-none">알림이 없습니다</div>`;
            $("#alarm-list").append(html);*/
            /*$("#alarm-list").load(window.location.href + "#alarm-list");*/
            window.location.reload();
        }
    }).catch((error) => {

    });
}