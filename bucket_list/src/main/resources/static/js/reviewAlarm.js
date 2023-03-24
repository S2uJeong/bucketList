$(document).ready(function() {
    $('#memberReviewContent').on('keyup', function() {
        $('#memberReviewContent_cnt').html("("+$(this).val().length+" / 100)");

        if($(this).val().length > 100) {
            $(this).val($(this).val().substring(0, 100));
            $('#test_cnt').html("(100 / 100)");
        }
    });
});

$(document).ready(function() {
    $('#bucketReviewContent').on('keyup', function() {
        $('#bucketReviewContent_cnt').html("("+$(this).val().length+" / 100)");

        if($(this).val().length > 100) {
            $(this).val($(this).val().substring(0, 100));
            $('#test_cnt').html("(100 / 100)");
        }
    });
});

async function memberReviewSave() {
    let targetMemberName = document.getElementById('memberReview_id').innerText;
    let rate = document.getElementById('memberRate').value;
    let content = document.getElementById('memberReviewContent').value;

    try {
        data = {"targetMemberName" : targetMemberName, "rate" : rate, "content" : content};
        let memberReviewRes = await axios.post('/api/v1/members/ratings', data, config);
        if(memberReviewRes.data.result == "duplicated"){
            alert("이미 작성된 리뷰입니다.");
        } else{
            $("#memberReview").modal("hide");
            $("#memberReview").find('form')[0].reset()

            let alarmId = $('#memberReview_alarmId').val();

            axios({
                method:"POST",
                url: '/alarm/read',
                params: {'alarmId':alarmId}
            }).then((res) => {
                data = res.data.result;
                if(data >= 1) {
                    $(`#alarm-${alarmId}`).remove();
                }
            }).catch((error) => {

            });
        }
    } catch (e) {
    }
}

async function bucketReviewSave() {
    let targetPostId = document.getElementById('bucketReview_id').innerText;
    let rate = document.getElementById('bucketRate').value;
    let content = document.getElementById('bucketReviewContent').value;

    try {
        data = {"targetPostId" : targetPostId, "rate" : rate, "content" : content};
        let bucketReviewRes = await axios.post('/api/v1/posts/reviews', data, config);
        if(bucketReviewRes.data.result == "duplicated"){
            alert("이미 작성된 리뷰입니다.");
        } else{
            $("#bucketReview").modal("hide");
            $("#bucketReview").find('form')[0].reset()
            let alarmId = $('#bucketReview_alarmId').val();

            axios({
                method:"POST",
                url: '/alarm/read',
                params: {'alarmId':alarmId}
            }).then((res) => {
                data = res.data.result;
                if(data >= 1) {
                    $(`#alarm-${alarmId}`).remove();
                }
            }).catch((error) => {

            });
        }
    } catch (e) {
    }
}