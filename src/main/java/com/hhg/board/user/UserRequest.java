package com.hhg.board.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest {

    @Data
    public static class JoinDTO {

        @NotEmpty(message = "이름을 입력 해 주세요.")
        private String username;

        @NotEmpty(message = "생년월일을 입력 해 주세요.")
        private String userBirthDate;

        @NotEmpty(message = "로그인 아이디를 입력 해 주세요.")
        private String userLoginId;

        @NotEmpty(message = "이메일은 필수 입니다")
        @Pattern(
                regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",
                message = "이메일 형식으로 작성해주세요"
        )
        private String userEmail;

        @NotEmpty(message = "비밀번호를 입력해주세요.") // null, 빈 문자열("") 허용 안함
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자 이내로 작성해주세요.")
        private String userPassword;

        @NotNull(message = "프로필 이미지를 등록 해주세요.")
        private MultipartFile profileImage;

        private String profileImagePath;

        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .userBirthDate(this.userBirthDate)
                    .userLoginId(this.userLoginId)
                    .userEmail(this.userEmail)
                    .userPassword(this.userPassword)
                    .profileImagePath(this.profileImagePath)
                    .build();
        }
    }

    @Data
    public static class LoginDTO {
        @NotEmpty(message = "로그인 아이디를 입력 해 주세요.")
        private String userLoginId;
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String userPassword;
    }
}
