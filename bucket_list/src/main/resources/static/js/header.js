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
        $("#user_li").hide();
    } else {
        $("#login_li").hide();
        $("#user_li").show();

        const payload = accessToken.split('.')[1];
        let userName = JSON.parse(decodeURIComponent(escape(window.atob(payload)))).userName;
        console.log(userName);
        document.getElementById("userName").innerText = userName;
    }

};

let code;
const config = {"Content-Type" : `application/json` };

//닉네임 중복 검사
async function checkUserName() {
    let userName = document.getElementById('form_userName').value;

    try {
        const response = await axios.post(
            '/join/checkUserName',
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
            '/join/email',
            {'email' : email} ,
            config);

        code = response.data.result;
        document.getElementById('error_email').style.display = 'none';

    } catch (error) {
        console.log(error);
        document.getElementById('error_email').style.display = 'block';

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
            const res = await axios.post('/join', data, config);

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
        const login_response = await axios.post('/login', data, config);
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
        console.log(error);
        const status = error.response.status

        if(status === 401) {
            const originRequest = error.config

            await axios.post('/reissue')
                .then(response => {
                    console.log('interceptor error='+response.data);
                    localStorage.setItem("accessToken", response.data);
                    originRequest.headers.authorization = 'Bearer ' + response.data;
                    return axios(originRequest);
                })
                .catch(error => {
                    console.log('재발급 실패, refresh token 만료');
                    localStorage.removeItem('accessToken')
                    window.location.href = '/';
                })
            return Promise.reject(error)
        }
        return Promise.reject(error);
    }
)
