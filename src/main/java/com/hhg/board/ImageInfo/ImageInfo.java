package com.hhg.board.ImageInfo;

import com.hhg.board.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;


@NoArgsConstructor
@Data
@Table(name = "image_info")
@Entity
public class ImageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgInfoId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private String category;

    // 이미지 경로 필드 추가
    @Column(nullable = false)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String instId;

    @CreationTimestamp
    private Timestamp instDate;

    @Transient
    private boolean isImageInfoOwner;

    @Builder
    public ImageInfo(Long imgInfoId, String title, String content, String category, String imagePath, User user, String instId, Timestamp instDate) {
        this.imgInfoId = imgInfoId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.imagePath = imagePath;
        this.user = user;
        this.instId = instId;
        this.instDate = instDate;
    }

    public boolean isOwner(Long sessionUserId) {
        return this.user.getUserId().equals(sessionUserId);
    }

}
