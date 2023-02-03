package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.MulticastChannel;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;


    // 게시글 폼에서 데이터 받아오기(Ajax 사용하여 받아옴)
    @PostMapping(value = "/detailpost" ,produces = "application/json")
    @ResponseBody
    public Response<PostIdResponse> getData(@RequestBody PostCreateRequest request){
//        Long userId = Long.valueOf(authentication.getName());
        Long userId = 1l;

        log.info("detailpost");
        String userName = "test";
        PostCreateResponse response = postService.create(request,userId);       // DB에 데이터 저장
        log.info("postId():"+response.getPostId());
        PostIdResponse postid = new PostIdResponse(response.getPostId());
        return Response.success(postid);
    }

    //== 전체조회 ==//
    @GetMapping("/list")
    @ResponseBody
    public Response<Page<PostReadResponse>> readAllPost(@PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC)
                                                        Pageable pageable, @RequestParam(value = "category", required = false) String category) {
        if(category == null){
            Page<PostReadResponse> postReadResponses = postService.readAll(pageable);
            log.info("PostList 보기 성공");
            return Response.success(postReadResponses);
        } else{
            Page<PostReadResponse> filterPosts = postService.filter(category, pageable);
            return Response.success(filterPosts);
        }
    }

    // 버킷리스트 필터
//    @GetMapping("list")
//    public Response<Page<PostReadResponse>> postFilter(@PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC)
//                                                       Pageable pageable, @RequestParam("category") String category ) {
//        Page<PostReadResponse> filterPosts = postService.filter(category);
//        return Response.success(filterPosts);
//    }

    //== model 사용 세부조회 ==//

//    @GetMapping("/{postId}")
//    public String readPost(@PathVariable(value = "postId") Long postId, Model model){
//        PostReadResponse postReadResponse = postService.read(postId);
//
//        String title = postReadResponse.getTitle();
//        String untile = postReadResponse.getUntilRecruit();
//        String eventStart = postReadResponse.getEventStart();
//        String eventEnd = postReadResponse.getEventEnd();
//        int cost = postReadResponse.getCost();
//        int entrantNum = postReadResponse.getEntrantNum();
//        String category = postReadResponse.getCategory();
//        String location = postReadResponse.getLocation();
//        String content = postReadResponse.getContent();
//        double lat = postReadResponse.getLat();
//        double lng = postReadResponse.getLng();
//
//
//        model.addAttribute("title",title);
//        model.addAttribute("untilRecruit",untile);
//        model.addAttribute("eventStart",eventStart);
//        model.addAttribute("eventEnd",eventEnd);
//        model.addAttribute("cost",cost);
//        model.addAttribute("entrantNum",entrantNum);
//        model.addAttribute("category",category);
//        model.addAttribute("content",content);
//        model.addAttribute("lat",lat);
//        model.addAttribute("lng",lng);
//        model.addAttribute("location",location);
//        model.addAttribute("apikey",postReadResponse.getAPI_KEY());
//        return "Post/PostDetail";
//    }



    @GetMapping(value = "/{postId}/json", produces = "application/json")
    @ResponseBody
    public Response<PostReadResponse> jsonreadPost(@PathVariable(value = "postId") Long postId){
        PostReadResponse postReadResponse = postService.read(postId);
        log.info("DB에서 데이터 호출 location :"+postReadResponse.getLocation());
        return Response.success(postReadResponse);
    }

    //== 삭제 ==//
    @DeleteMapping("/{postId}")
    public Response<PostDeleteResponse> deletePost(
            @PathVariable("postId") long postId ,Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());

        PostDeleteResponse postDeleteResponse = postService.delete(postId,userId);
        log.info("Post 삭제 성공");
        return Response.success(postDeleteResponse);
    }

    // S3에 파일 업로드
    @PostMapping("/{postId}/files")
    public Response<UploadFileResponse> upload(@PathVariable("postId") Long postId,
                                               @RequestParam MultipartFile multipartFile) throws IOException {
        return Response.success(postService.UploadFile(multipartFile));
    }
    // S3 파일 삭제
    @DeleteMapping("/{postId}/files/{fileId}")
    public Response<DeleteFileResponse> delete(@PathVariable("postId") Long postId,
                                               @PathVariable("fileId") Long fileId,
                                               @RequestParam String filePath) {
        return Response.success(postService.deleteFile(fileId, filePath));
    }

    //== 좋아요 확인 ==//
    @GetMapping("/{postId}/likes/check")
    public Response<Integer> checkLike(@PathVariable("postId") long postId, Authentication authentication) {
        int checkLike = postService.checkLike(postId, Long.valueOf(authentication.getName()));
        return Response.success(checkLike);
    }

    //== 좋아요 개수 ==//
    @GetMapping("/{postId}/likes")
    public Response<Long> countLike(@PathVariable Long postId) {
        Long cntLike = postService.countLike(postId);
        return Response.success(cntLike);
    }

    //== 좋아요 누르기 ==//
    @PostMapping("/{postId}/likes")
    public Response<Integer> clickLike(@PathVariable("postId") long postId, Authentication authentication) {
        int result = postService.clickLike(postId, Long.valueOf(authentication.getName()));
        return Response.success(result);
    }

    //== 마이피드 ==//
    // 좋아요한
    @GetMapping("/my/likes")
    public Response<Page<PostReadResponse>> myFeedLike(@PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC)
                                           Pageable pageable, Authentication authentication) {
        Page<PostReadResponse> likePosts = postService.myFeedLike(pageable, Long.valueOf(authentication.getName()));
        return Response.success(likePosts);
    }

    // 작성한
    @GetMapping("/my/create")
    public Response<Page<PostReadResponse>> myFeedCreate(@PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC)
                                           Pageable pageable, Authentication authentication) {
        Page<PostReadResponse> createPosts = postService.myFeedCreate(pageable, Long.valueOf(authentication.getName()));
        return Response.success(createPosts);
    }

    // 신청한
    @GetMapping("/my/apply")
    public Response<Page<PostReadResponse>> myFeedApply(@PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC)
                                           Pageable pageable, Authentication authentication) {
        Page<PostReadResponse> applyPosts = postService.myFeedApply(pageable, Long.valueOf(authentication.getName()));
        return Response.success(applyPosts);
    }
}

