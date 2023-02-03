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
        let userName = JSON.parse(atob(payload)).userName;
        document.getElementById("userName").innerText = userName;
    }
};

