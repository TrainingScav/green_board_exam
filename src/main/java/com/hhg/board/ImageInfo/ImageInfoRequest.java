package com.hhg.board.ImageInfo;

import com.hhg.board.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class ImageInfoRequest {

    @Data
    public static class SaveImageInfoDTO {

        @NotEmpty(message = "제목을 입력 해주세요.")
        @Size(min = 10, max = 50, message = "제목을 최소 10자, 최대 50자까지 입력 해주세요.")
        private String title;

        @NotEmpty(message = "설명을 입력 해주세요.")
        @Size(min = 10, max = 500, message = "설명을 최소 10자, 최대 500자까지 입력 해주세요.")
        private String content;

        @NotEmpty(message = "카테고리를 선택 해 주세요.")
        private String category;

        @NotNull(message = "이미지 파일을 업로드 해주세요.")
        private MultipartFile imageFile;

        private String imagePath;

        private String instId;

        public ImageInfo toEntity(User user) {
            return ImageInfo.builder()
                    .title(this.title)
                    .content(this.content)
                    .category(this.category)
                    .imagePath(this.imagePath)
                    .user(user)
                    .instId(user.getUserLoginId())
                    .build();
        }
    }

    @Data
    public static class UpdateImageInfoDTO {

        @NotEmpty(message = "제목을 입력 해주세요.")
        @Size(min = 10, max = 50, message = "제목을 최소 10자, 최대 50자까지 입력 해주세요.")
        private String title;

        @NotEmpty(message = "설명을 입력 해주세요.")
        @Size(min = 10, max = 500, message = "설명을 최소 10자, 최대 500자까지 입력 해주세요.")
        private String content;

        @NotEmpty(message = "카테고리를 선택 해 주세요.")
        private String category;

        private MultipartFile imageFile;

        private String imagePath;

        private String changeImageYn;

        private String instId;


        public ImageInfo toEntity(User user) {
            return ImageInfo.builder()
                    .title(this.title)
                    .content(this.content)
                    .category(this.category)
                    .imagePath(this.imagePath)
                    .user(user)
                    .instId(user.getUsername())
                    .build();
        }
    }
}
