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
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE file SET deleted_at = CURRENT_TIMESTAMP where file_id = ?") // base Entity 상속 받을 예정
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long id;
    private String uploadFileName; // 사용자가 설정한 파일 이름
    private String storeFileUrl; // 파일 이름 중복 방지 위한 저장된 파일의 URL을 나타내는 필드 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")  // post도 file을 알아야 하나 ? -> OneToMany 해줘야하나
    private Post post;

    public File(String uploadFileName, String storeFileUrl) {
        this.uploadFileName = uploadFileName;
        this.storeFileUrl = storeFileUrl;
    }
}