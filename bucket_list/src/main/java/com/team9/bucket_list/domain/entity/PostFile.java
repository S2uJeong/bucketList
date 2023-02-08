package com.team9.bucket_list.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
/*@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE file SET deleted_at = CURRENT_TIMESTAMP where file_id = ?")*/
public class PostFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long id;
    private String uploadFileName; // 사용자가 설정한 파일 이름
    private String awsS3FileName; // DB에 저장된 파일의 URL


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    public static PostFile save(String uploadFileName, String awsS3FileName, Post post) {
        return PostFile.builder()
                .uploadFileName(uploadFileName)
                .awsS3FileName(awsS3FileName)
                .post(post)
                .build();
    }

}