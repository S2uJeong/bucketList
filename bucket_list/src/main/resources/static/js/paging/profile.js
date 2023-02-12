// 전역변수
let review_list = [];
let page_info = [];

const showPageCnt = 5; // 화면에 보일 페이지 번호 개수

const urlSearch = new URL(window.location.href).search;
console.log(urlSearch)
const memberId = new URL(window.location.href).pathname.replace("/profile/", "");
let url_href = "?page=";  // 페이지 넘버의 href를 정할 수 있도록 "page="을 붙임

// 화면이 로딩되면 실행되는 부분
const todosUrl = '/member/rating/' + memberId + urlSearch;
console.log("todosUrl " + todosUrl)
axios.get(todosUrl)
    .then(res => {
        page_info = res.data.result;
        console.info(page_info);
        review_list = res.data.result.content;
        console.log(review_list);

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
    let html = "<div class=\"mb-3\">\n" +
        "                <div>\n" +
        "                       <p class=\"review-username\">{리뷰 작성 유저}</p>\n" +
        "                       <p class=\"review-rate\">점수 : {점수}</p>\n" +
        "                       <p class=\"review-date\">{작성일자}</p>\n" +
        "                   </div>\n" +
        "                   <div>\n" +
        "                       <p class=\"review-content\">{내용}</p>\n" +
        "                   </div>\n" +
        "<hr>\n" +
        "               </div>";

    let parent = document.getElementById('review_list');
    parent.innerHTML = "";

    review_list.forEach(function (review) {
        let html_result = html.replace("{리뷰 작성 유저}", review.writerName)
            .replaceAll("{점수}", review.rate)
            .replace("{작성일자}", review.createdAt)
            .replace("{내용}", review.content);

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
        "    <a class=\"page-link " + (start == currentPage ? 'active' : '') + "\" href='/profile/" + memberId + url_href + (start - 1) + "'>" + start + "</a>\n" +
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
        window.location.href = "/profile/" + memberId + url_href + 0;
    } else if (id == 'prev_page') {
        let arrPages = [];
        $('li.page-item>a.page-link').each(function(idx, item) {
            arrPages.push(Number($(this).text()));
        });
        const prevPage = Math.min(...arrPages) - showPageCnt;
        console.log("prevPage" + prevPage);
        window.location.href = "/profile/" + memberId + url_href + (prevPage - 1);
    } else if (id == 'next_page') {
        let arrPages = [];
        $('li.page-item>a.page-link').each(function(idx, item) {
            arrPages.push(Number($(this).text()));
        });
        console.log("next_page" + arrPages);

        const nextPage = Math.max(...arrPages) + 1;
        console.log("nextPage" + nextPage);
        window.location.href = "/profile/" + memberId + url_href + (nextPage - 1);
    } else if (id == 'last_page') {
        const lastPage = Math.floor((totalPage - 1) / showPageCnt) * showPageCnt + 1;
        console.log("lastPage" + lastPage);
        window.location.href = "/profile/" + memberId + url_href + (lastPage - 1);
    }
});