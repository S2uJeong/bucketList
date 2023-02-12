const lsAccessToken = localStorage.getItem("accessToken");
const lsPayload = lsAccessToken.split('.')[1];
const lsMemberId = JSON.parse(atob(lsPayload)).memberId;

// Get the modal
var modal = document.getElementById("myModal");

// Get the button that opens the modal
var btn = document.getElementById("pic-btn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal
btn.onclick = function () {
    modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function () {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

/*<!-- 이미지 파일 넣는 부분 (모달창 내부) -->
// Get elements
var fileInput = document.getElementById("fileInput");
var uploadBtn = document.getElementById("uploadBtn");
var imagePreview = document.getElementById("imagePreview");

// Handle file input change
fileInput.addEventListener("change", function () {
    // Get selected files
    var files = fileInput.files;

    // Clear preview
    imagePreview.innerHTML = "";

    // Display thumbnails
    for (var i = 0; i < files.length; i++) {
        // Only process image files
        if (!files[i].type.startsWith("image")) {
            continue;
        }

        // Create thumbnail
        var thumbnail = document.createElement("div");
        thumbnail.classList.add("thumbnail");
        var image = document.createElement("img");
        image.src = URL.createObjectURL(files[i]);
        thumbnail.appendChild(image);
        imagePreview.appendChild(thumbnail);
    }
});*/

let fileInput = document.getElementById("fileInput");

fileInput.addEventListener("change", function() {
    const imageSrc = URL.createObjectURL(fileInput.files[0]);

    $('.img-preview').css("background-image",`url(${imageSrc})`);
});

/*// Handle upload button click
uploadBtn.addEventListener("click", function () {
    // Get selected files
    var files = fileInput.files;

    // Check if files were selected
    if (files.length === 0) {
        alert("Please select some files to upload.");
        return;
    }

    // Upload files here (using fetch, XHR, or any other method)
    // ...

    // Clear file input
    fileInput.value = "";

    // Clear preview
    imagePreview.innerHTML = "";
});*/

function uploadPic() {
/*    const FileElement = document.querySelector('#fileInput');

    const getData = async () => {
        try{
            const formData = new FormData();
            const header = { "Content-Type": "multipart/form-data"} ;

            formData.append("file", FileElement.files[0]);

            const url = `/profile/${lsMemberId}/edit`;

            const response = await axios.post(url, formData, header);

            console.log(response)
        }
        catch (error){
            console.error(error);
        }
    }*/
    let form = $('#file-form')[0];

    // Create an FormData object
    let data = new FormData(form);
    let editImgData;

    axios({
        method:"POST",
        url: `/profile/${lsMemberId}/edit`,
        data:data,
        header:{"Content-Type": "multipart/form-data"},
    }).then((res)=> {
        editImgData = res.data.result;
        console.log(res);

        if(editImgData.uploadFileName == '기본사진.png')
            $('.pic-wrap').css('backgroundImage',`url(${editImgData.awsS3FileName})`);
        else
            $('.pic-wrap').css('backgroundImage',`url(https://bucketlist-post-image-bucket.s3.ap-northeast-2.amazonaws.com/${editImgData.awsS3FileName})`);

        modal.style.display = "none";
    }).catch((error)=> {
        console.log(error);
    });


    /*$('#file-form').attr("action", `/profile/${lsMemberId}/edit`);
    $('#file-form').submit();

    alert("프로필 사진이 변경되었습니다");*/
}

/*
$('#fileInput').change(function (){
    let files = $('#fileInput')[0].files[0];
    console.log(files);
});
*/


window.onload = function () {
    let profileData;
    $('#if').css("display","none");

    axios({
        method:"GET",
        url: `/profile/${lsMemberId}/json`,
    }).then((res)=> {
        profileData = res.data.result;

        if(profileData.uploadFileName == '기본사진.png')
            $('.pic-wrap').css('backgroundImage',`url(${profileData.awsS3FileName})`);
        else
            $('.pic-wrap').css('backgroundImage',`url(https://bucketlist-post-image-bucket.s3.ap-northeast-2.amazonaws.com/${profileData.awsS3FileName})`);

        $('#profile-username').text(profileData.memberName);
        $('#profile-email').text(profileData.email);
        $('#profile-avg-rate').text(profileData.avgRate);
    }).catch((error)=> {
        console.log(error);
    });
}


