//heart 좋아요 클릭시! 하트 뿅
$(function(){
    var $likeBtn =$('.icon.heart');

    $likeBtn.click(function(){
        $likeBtn.toggleClass('active');

        if($likeBtn.hasClass('active')){
            $(this).find('img').attr({
                'src': '/assets/img/like/full-heart.png',
                alt:'찜하기 완료'
            });


        }else{
            $(this).find('i').removeClass('fas').addClass('far')
            $(this).find('img').attr({
                'src': '/assets/img/like/blank-heart.png',
                alt:"찜하기"
            })
        }
    })
})

// postCreate
window.onload = function(){
    document.getElementById("address_kakao").addEventListener("click", function(){ //주소입력칸을 클릭하면
        //카카오 지도 발생
        new daum.Postcode({
            oncomplete: function(data) { //선택시 입력값 세팅
                document.getElementById("address_kakao").value = data.address; // 주소 넣기
                document.querySelector("input[name=post]").focus(); //상세입력 포커싱
            }
        }).open();
    });
}


<!-- 라디오 버튼 클릭시 동작 -->
function showDiv(element){
    var tag = document.getElementsByClassName("order-1 order-md-0");   // class 이름이 box인 값들을 가져옴

    for(var i =0; i<tag.length; i++){     // tag[0] = koreanBox, tag[1] = foreignerBox
        if(element.id+"Box"== tag[i].id)    // korean+"Box"가 tag[0]의 값과 같을때 해당 div 출력
            tag[i].style.display = "block";
        else                                // 다르다면 div 숨기기
            tag[i].style.display = "none";
    }
}

$(document).ready(function(){
    $("#submit").click(function() {

        // 게시물 배열 선언
        let arr = [];

        $('input[name="post"]').each(function(index,item){ // 모든 값 배열에 저장
            arr.push($(item).val());
        });


        var finddata = $("#address_kakao").val();   // 국내 주소 값 저장

        if(finddata.length <1){     // 만약 국내 주소 값의 크기가 1보다 작을때, 즉 국내에서 데이터를 작성한 것이 아닐때
            alert("world data")
            var WorldData = {       // 해외 데이터
                "title" : arr[9],       // 제목
                "untilRecruit" : arr[10],    // 모집종료 날짜
                "eventStart" : arr[11],       // 시작일
                "eventEnd" : arr[12],          // 종료일
                "cost" : arr[13],              // 비용
                "entrantNum" : arr[14],         // 모집인원
                "category" : arr[15],       // 카테고리
                "location" : arr[16],        // 장소
                "content" : arr[17]         // 내용
            }

            //ajax 호출
            $.ajax({
                type        :   "post",
                url         :   "/post/detailpost",
                headers : {
                    "Content-Type" : "application/json"
                },
                dataType    :   "text",
                // contentType :   "application/json",
                data        :   JSON.stringify(WorldData),
                async : false,
                success     :   function(data){
                    console.log(data);
                    let url = '/map?postId=${data}';
                    window.location.replace(url);
                }
            });
        }

        else{           // 국내에서 작성했을때
            alert("korea data")
            var KoreaData = {         // 한국 데이터
                "title" : arr[0],       // 제목
                "untilRecruit" : arr[1],    // 모집종료 날짜
                "eventStart" : arr[2],       // 시작일
                "eventEnd" : arr[3],          // 종료일
                "cost" : arr[4],              // 비용
                "entrantNum" : arr[5],         // 모집인원
                "category" : arr[6],       // 카테고리
                "location" : arr[7],        // 장소
                "content" : arr[8]         // 내용
            }

            //ajax 호출
            $.ajax({
                type        :   "post",
                url         :   "/post/detailpost",
                headers : {
                    "Content-Type" : "application/json",
                },
                dataType    :   "text",
                // contentType :   "application/json",
                data        :   JSON.stringify(KoreaData),
                async : false,
                success     :   function(data){      // server에서 결과값 받아옴
                    let url = "/post/"+data;
                    window.location.replace(url);     // ajax에서 redirect 진행
                }
            });
        }
    })
});

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

