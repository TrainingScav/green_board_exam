package com.hhg.board.user;

import com.hhg.board.ImageInfo.ImageInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Table(name = "user_info")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 10)
    private String username;
    @Column(nullable = false, length = 8)
    private String userBirthDate;

    @Column(nullable = false, length = 20, unique = true)
    private String userLoginId;
    @Column(nullable = false, length = 100, unique = true)
    private String userEmail;
    @Column(nullable = false)
    private String userPassword;

    // 이미지 경로 필드 추가
    @Column(nullable = false)
    private String profileImagePath;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public User(Long userId, String username, String userBirthDate, String userLoginId, String userEmail, String userPassword, String profileImagePath, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.userBirthDate = userBirthDate;
        this.userLoginId = userLoginId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.profileImagePath = profileImagePath;
        this.createdAt = createdAt;
    }
}
