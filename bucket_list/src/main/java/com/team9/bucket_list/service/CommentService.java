package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.comment.CommentCreateRequest;
import com.team9.bucket_list.domain.dto.comment.CommentCreateResponse;
import com.team9.bucket_list.domain.dto.comment.CommentListResponse;
import com.team9.bucket_list.domain.entity.Comment;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.PostRepository;
import com.team9.bucket_list.repository.comment.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // -------------- 예외처리 ------------------
    public Comment commentDBCheck(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));
    }

//      Member와 합쳐지면 현재 유저의 권한이 관리자인지 아닌지 판단
//    public Boolean rolecheck(Member member, String userName){
//        if((member.getRole() != UserRole.ADMIN)&&(member.getUserName().equals(userName))) {      // 현재 토큰의 유저의 권한이 ADMIN이 아닐때
//            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
//        }
//        return true;
//    }

    // Member와 합쳐지기 전까지 임시로 사용
        public Boolean rolecheck(String member, String userName){
        if(!member.equals(userName)) {      // 현재 토큰의 유저의 권한이 ADMIN이 아닐때
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        return true;
        }

    // 1. 댓글 생성
    @CacheEvict(value = "comments", key = "#postId")
    public CommentCreateResponse commentCreate(Long postId, CommentCreateRequest request,String userName) {
//            Member member = memberRepository.findById(c.getWriterId())            Member 합치면 연동
//                    .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));


        Long writerId = 1l;         // member와 연동하지 않아서 임시용


        Comment parentComment = request.getParentId() != null ?              // parentid를 입력받았으면 comment 객체 생성, 입력이 없다면 null 반환
                commentDBCheck(request.getParentId()) : null;

        Comment comment = request.toEntity(post,parentComment,writerId);       // Dto -> Entity

        commentRepository.save(comment);

        CommentCreateResponse response = new CommentCreateResponse(comment,userName);
        return response;
    }

    // 2. 댓글 리스트 출력
    @Cacheable(value = "comments", key = "#postId")
    public List<CommentListResponse> commentList(Long postId){
        postRepository.findById(postId)
              .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));   // 게시물 존재 확인
        return structureChange(commentRepository.findCommentByPostId(postId));
    }

    // 3. 댓글 수정
    @CacheEvict(value = "comments", key = "#postId")
    @Transactional
    public List<CommentListResponse> updateComment(Long postId,Long commentId, CommentCreateRequest request,String userName){
//     Member member = memberRepository.findById(c.getWriterId())            Member 합치면 연동
//          .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));

        postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));   // 게시물 존재 확인

        Comment comment = commentDBCheck(commentId);        // Comment 존재 확인

        rolecheck("test",userName);                 // 댓글 작성자와 현재 유저 비교

        if(!request.getContent().equals(comment.getContent())){     // 서버에 저장되어 있는 데이터와 유저가 새로 입력한 데이터가 다를경우 덮어씌움
            comment.update(request.getContent());      // Jpa 영속성을 활용한 update 기능 활용
        }


        return structureChange(commentRepository.findCommentByPostId(postId));
    }


    // 4. 댓글 삭제
    @CacheEvict(value = "comments", key = "#postId")
    @Transactional
    public List<CommentListResponse> deleteComment(Long postId,Long commentId,String userName){
//     Member member = memberRepository.findById(c.getWriterId())            Member 합치면 연동
//          .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));

        postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));   // 게시물 존재 확인

        Comment comment =  commentRepository.findCommentByIdWithParent(commentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));

        rolecheck("test",userName);                 // 댓글 작성자와 현재 유저 비교

        commentRepository.delete(comment);

        return structureChange(commentRepository.findCommentByPostId(postId));
    }



    public List<CommentListResponse> structureChange(List<Comment> comments){    // List<Comment> -> List<CommentDto>
        List<CommentListResponse> result = new ArrayList<>();       // 댓글 전체를 담을 리스트
        Map<Long,CommentListResponse> map = new HashMap<>();        // 댓글과 대댓글을 연결시켜주기 위한 임시 map
        comments.stream().forEach(c -> {                            // Comment 형식으로 되어있는 댓글 전체 리스트를 stream.foreach를 통해 모든값을 CommentDto로 변환함
//            Member member = memberRepository.findById(c.getWriterId())            Member 합치면 연동
//                    .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
//            String userName = member.getUserName();
            String userName = "usernametest";
            CommentListResponse dto = CommentListResponse.EntityToDto(c,userName);     // Comment에서 원하는 값만 추출하여 Dto로 변환함
            map.put(c.getId(),dto);                                                    // map에 (commentId, dto) 형식으로 저장
            if(c.getParent() != null)
                map.get(c.getParent().getId()).getChildren().add(dto);                 // 만약 댓글 entity에 부모댓글이 null값이 아니라면 현재 comment의 parentId의 데이터를 가져와 연관맵핑 되어 있는 childcomment list에 넣어준다
            else
                result.add(dto);                                    // 부모id가 없는 댓글 즉, 대댓글이 아닌경우에만 result list에 넣는다.
            }
        );
        return result;
    }

}
