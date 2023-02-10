// 전역변수
let post_list = [];
let page_info = [];

const showPageCnt = 5; // 화면에 보일 페이지 번호 개수

const urlSearch = new URL(window.location.href).search;
console.log(urlSearch)
let url_href = urlHref(urlSearch);  // 페이지 넘버의 href를 정할 수 있도록 "page="을 붙임

// 화면이 로딩되면 실행되는 부분
const todosUrl = '/post/list' + urlSearch;
console.log("todosUrl " + todosUrl)
axios.get(todosUrl)
    .then(res => {
        page_info = res.data.result;
        console.info(page_info);
        post_list = res.data.result.content;

        // pageable 사용으로 url의 page 번호 가져오기
        setTable();
        if(!urlSearch.includes("page=")){
            setPaging(1);
        } else{
            setPaging(Number(urlSearch.replace(url_href, "")) + 1);
        }
    })
    .catch(err => console.error(err))
    .then(() => {
        // finally
    });

/**
 * 데이터를 세팅합니다.
 */
function setTable() {
    let html = "<div class=\"col-md-6 col-xl-4 mb-5\">\n" +
        "            <div class=\"card card-hover\">\n" +
        "              <a href=\"post/{포스트 아이디}\" class=\"position-relative\">\n" +
        "                <img class=\"card-img-top lazyestload\" data-src=\"{이미지 URL}\" src=\"{이미지 URL}\" alt=\"Card image cap\">\n" +
        "                <div class=\"card-img-overlay card-hover-overlay rounded-top d-flex flex-column\">\n" +
        "                  <div class=\"badge {배경색} badge-rounded-circle\">\n" +
        "                    <span class=\"d-block\">{모집상태}</span>\n" +
        "                  </div>\n" +
        "                </div>\n" +
        "              </a>\n" +
        "\n" +
        "              <div class=\"card-body px-4\">\n" +
        "                <h5>\n" +
        "                  <a href=\"post/{포스트 아이디}\" class=\"card-title text-uppercase\">{제목}</a>\n" +
        "                </h5>\n" +
        "                <h6 class=\"mt-n2\">\n" +
        "                  {주최자 이름}\n" +
        "                </h6>\n" +
        "                <p class=\"mb-1\">모집 기간 : ~ {모집 마감 날짜}</p>\n" +
        "                <p class=\"mb-1\">일정 : {일정 시작 날짜} ~ {일정 종료 날짜}</p>\n" +
        "                <p class=\"mb-1\">비용 : {비용}</p>\n" +
        "                <p class=\"mb-1\">장소 : {장소}</p>\n" +
        "              </div>\n" +
        "            </div>\n" +
        "          </div>";

    let parent = document.getElementById('list_container');
    parent.innerHTML = "";



    // js에서 css를 하기 위해서 만든 부분
    function getImgBackgroundForm() {
        const props = "--background-image";

        const root = document.documentElement; // html의 모든 요소를 root에 저장
        const rootStyle = getComputedStyle(root); // root에 있던 style의 :root에 있는 객체를 rootStyle에 저장

        return rootStyle; // --background-image
    }

    function setImgBackgroundForm() {
        const postImage = document.querySelector("#list_container");
        postImage.style.backgroundImage = getImgBackgroundForm();
    }



    post_list.forEach(function (post) {
        let postImage;
        <!-- 이미지 출력 경로 지정 -->
        if(post.fileName == "noImage"){
            postImage = "/assets/img/noImage.png";
        }else {
            postImage = 'https://bucketlist-post-image-bucket.s3.ap-northeast-2.amazonaws.com/' + post.fileName;
        }

        let html_result = html.replace("{제목}", post.title)
            .replaceAll("{포스트 아이디}", post.postId)
            .replace("{주최자 이름}", post.userName)
            .replace("{비용}", post.cost)
            .replace("{장소}", post.location)
            .replace("{일정 시작 날짜}", post.eventStart)
            .replace("{일정 종료 날짜}", post.eventEnd)
            .replace("{모집 마감 날짜}", post.untilRecruit)
            .replace("{이미지 URL}", postImage);

        if (post.status === 'JOIN') {
            html_result = html_result.replace("{모집상태}", '모집중')
                .replace("{배경색}", 'bg-primary');
        } else if (post.status === 'JOINCOMPLETE') {
            html_result = html_result.replace("{모집상태}", '모집완료')
                .replace("{배경색}", 'bg-success');
        } else if (post.status === 'PROCESS') {
            html_result = html_result.replace("{모집상태}", '진행중')
                .replace("{배경색}", 'bg-danger');
        } else if (post.status === 'COMPLETE') {
            html_result = html_result.replace("{모집상태}", '진행완료')
                .replace("{배경색}", 'bg-info');
        }

        parent.innerHTML += html_result;
    });
}

/**
 * 페이징 정보를 세팅합니다.
 * @param {int} pageNum - Page Number
 */
function setPaging(pageNum) {
    const currentPage = pageNum;
    const totalPage = page_info.totalPages;

    // html에 페이지 번호를 세팅
    let start = Math.floor((currentPage - 1) / showPageCnt) * showPageCnt + 1;
    let sPagesHtml = '';
    sPagesHtml += "<li class=\"page-item\">\n" +
    "    <a id=\"first_page\" class=\"page-link-i\">\n" +
        "        <i class=\"fas fa-angle-double-left d-none d-md-inline-block me-md-1\"\n" +
        "           aria-hidden=\"true\"></i>\n" +
        "</a>\n" +
        "</li>\n" +
        "\n" +
        "<li class=\"page-item\">\n" +
        "    <a id=\"prev_page\" class=\"page-link-i\">\n" +
        "        <i class=\"fas fa-angle-left d-none d-md-inline-block me-md-1\" aria-hidden=\"true\"></i>\n" +
        "    </a>\n" +
        "</li>\n";

    for (const end = start + showPageCnt; start < end && start <= totalPage; start++) {
        console.log("start : ", start, "currentPage : ", currentPage, start == currentPage)
        sPagesHtml += "<li class=\"page-item\">\n" +
        "    <a class=\"page-link " + (start == currentPage ? 'active' : '') + "\" href='/post" + url_href + (start - 1) + "'>" + start + "</a>\n" +
        "</li>\n";
    }

    sPagesHtml += "<li class=\"page-item\">\n" +
        "    <a id=\"next_page\" class=\"page-link-i\">\n" +
        "        <i class=\"fas fa-angle-right d-none d-md-inline-block me-md-1\"\n" +
        "           aria-hidden=\"true\"></i>\n" +
        "</a>\n" +
        "</li>\n" +
        "\n" +
        "<li class=\"page-item\">\n" +
        "    <a id=\"last_page\" class=\"page-link-i\">\n" +
        "        <i class=\"fas fa-angle-double-right d-none d-md-inline-block me-md-1\" aria-hidden=\"true\"></i>\n" +
        "    </a>\n" +
        "</li>\n";

    $('ul.pagination').html(sPagesHtml);

    showAllIcon();

    // 현재 페이지가 5(보여질 페이지번호 개수)보다 작은 경우
    // 첫 페이지와 이전 페이지로 이동하는 Icon을 보여줄 필요가 없기 때문에 숨깁니다.
    if (currentPage <= showPageCnt) {
        $('#first_page').hide();
        $('#prev_page').hide();
    }
    // 총 페이지 번호가 5(보여질 페이지번호 개수)보다 작거나
    // 페이징 영역에 마지막 페이지 번호를 보여주고 있으면
    // 마지막 페이지와 다음 페이지로 이동하는 Icon을 숨깁니다.
    if (
        totalPage <= showPageCnt ||
        Math.floor((currentPage - 1) / showPageCnt) * showPageCnt + showPageCnt + 1 > totalPage
    ) {
        $('#next_page').hide();
        $('#last_page').hide();
    }
}

// Icon(첫 페이지, 이전 페이지, 다음 페이지, 끝 페이지)을 모두 보여줍니다.
function showAllIcon() {
    $('#first_page').show();
    $('#prev_page').show();
    $('#next_page').show();
    $('#last_page').show();
}

function urlHref(urlSearch) {
    if (urlSearch == "" || urlSearch.startsWith("?page=")) {
        return "?page=";
    } else if (urlSearch.includes('&')) {
        return urlSearch.split("&")[0] + "&page=";
    } else if (urlSearch.startsWith("?category=")) {
        return urlSearch + "&page=";
    }
}

$(document).on('click', 'ul.pagination>li.page-item>a', function() {
    if (!$(this).hasClass('active')) {
        $(this).parent().parent().find('li.page-item>a.active').removeClass('active');
        $(this).addClass('active');
        console.log(Number($(this).text()));
        setTable();
    }
});

$(document).on('click', 'ul.pagination>li.page-item>a.page-link-i', function() {
    const totalPage = page_info.totalPages;
    const id = $(this).attr('id');
    console.log("id" + id);

    if (id == 'first_page') {
        window.location.href = "/post" + url_href + 0;
    } else if (id == 'prev_page') {
        let arrPages = [];
        $('li.page-item>a.page-link').each(function(idx, item) {
            arrPages.push(Number($(this).text()));
        });
        const prevPage = Math.min(...arrPages) - showPageCnt;
        console.log("prevPage" + prevPage);
        window.location.href = "/post" + url_href + (prevPage - 1);
    } else if (id == 'next_page') {
        let arrPages = [];
        $('li.page-item>a.page-link').each(function(idx, item) {
            arrPages.push(Number($(this).text()));
        });
        console.log("next_page" + arrPages);

        const nextPage = Math.max(...arrPages) + 1;
        console.log("nextPage" + nextPage);
        window.location.href = "/post" + url_href + (nextPage - 1);
    } else if (id == 'last_page') {
        const lastPage = Math.floor((totalPage - 1) / showPageCnt) * showPageCnt + 1;
        console.log("lastPage" + lastPage);
        window.location.href = "/post" + url_href + (lastPage - 1);
    }
});