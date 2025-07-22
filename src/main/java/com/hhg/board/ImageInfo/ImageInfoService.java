package com.hhg.board.ImageInfo;

import com.hhg.board._core.errors.exception.Exception404;
import com.hhg.board._core.utils.ImageUpload;
import com.hhg.board.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ImageInfoService {

    private final ImageUpload imageUpload;

    private final ImageInfoJpaRepository imageInfoJpaRepository;

    // 이미지 정보 목록
    public List<ImageInfo> getImageInfoList() {
        List<ImageInfo> imageInfoList = imageInfoJpaRepository.findAll();

        return imageInfoList;
    }

    // 이미지 정보 저장
    @Transactional
    public void saveImageInfo(ImageInfo imageInfo) {

        imageInfoJpaRepository.save(imageInfo);
    }

    // 이미지 정보 찾기 (상세)
    public ImageInfo getImageInfo(Long imageInfoId) {
        ImageInfo imageInfo = imageInfoJpaRepository.findById(imageInfoId).orElseThrow(() -> {
            throw new Exception404("해당 게시물이 존재하지 않습니다.");
        });

        return imageInfo;
    }

    // 이미지 정보 삭제
    @Transactional
    public void deleteImageInfo(Long imageInfoId) {

        ImageInfo targetImageInfo = imageInfoJpaRepository.findById(imageInfoId).orElseThrow(() -> {
            throw new Exception404("이미지 게시물 정보를 찾지 못했습니다.");
        });

        imageUpload.deleteImage(targetImageInfo.getImagePath(), "imageInfo");
        imageInfoJpaRepository.delete(targetImageInfo);
    }

    // 이미지 정보 수정
    @Transactional
    public void updateImageInfo(Long imageInfoId, User user, ImageInfoRequest.UpdateImageInfoDTO updateImageInfoDTO) throws IOException {

        ImageInfo targetImageInfo = imageInfoJpaRepository.findById(imageInfoId).orElseThrow(() -> {
            throw new Exception404("해당 이미지 정보를 찾지 못했습니다.");
        });

        // 이미지가 변경 됐을 경우
        if (updateImageInfoDTO.getChangeImageYn().equals("Y")) {
            // 기존에 등록된 이미지 삭제
            imageUpload.deleteImage(targetImageInfo.getImagePath(), "imageInfo");
            // 새로운 이미지 등록 작업
            String imgDir = imageUpload.uploadImage(updateImageInfoDTO.getImageFile(), "imageInfo");
            // DTO set
            updateImageInfoDTO.setImagePath(imgDir);

            targetImageInfo.setTitle(updateImageInfoDTO.getTitle());
            targetImageInfo.setContent(updateImageInfoDTO.getContent());
            targetImageInfo.setCategory(updateImageInfoDTO.getCategory());
            targetImageInfo.setImagePath(updateImageInfoDTO.getImagePath());
        } else {
            targetImageInfo.setTitle(updateImageInfoDTO.getTitle());
            targetImageInfo.setContent(updateImageInfoDTO.getContent());
            targetImageInfo.setCategory(updateImageInfoDTO.getCategory());
        }
    }
}
