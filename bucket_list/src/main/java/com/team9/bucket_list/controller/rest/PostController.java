package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


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


    @GetMapping(value = "/{postId}/json", produces = "application/json")
    @ResponseBody
    public Response<PostReadResponse> jsonreadPost(@PathVariable(value = "postId") Long postId){
        PostReadResponse postReadResponse = postService.read(postId);
        log.info("DB에서 데이터 호출 location :"+postReadResponse.getLocation());
        return Response.success(postReadResponse);
    }

    // 클라이언트에서 데이터 받아와서 수정
    @PostMapping(value = "/{postId}/update" ,produces = "application/json")
    public void updatePost( @PathVariable Long postId, @RequestBody PostUpdateRequest request)  {
        // update 메서드를 통해 request 내용대로 수정해준다. 반환값 : post Entity
        Long userId = 1l;
        log.info(request.toString());
        log.info("postId:"+postId);
        postService.update(request,postId,userId);
        log.info("🔵 Post 수정 성공");
    }

    //== 삭제 ==//
    @DeleteMapping("/{postId}")
    public Long deletePost(
            @PathVariable("postId") long postId ) {
//        Long userId = Long.valueOf(authentication.getName());
        Long userId = 1l;
        postService.delete(postId,userId);
        log.info("Post 삭제 성공");
        return 1l;
    }

   /* // S3에 파일 업로드
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
    }*/

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
