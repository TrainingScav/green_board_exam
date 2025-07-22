package com.hhg.board._core.utils;


import com.hhg.board.ImageInfo.ImageInfo;
import com.hhg.board._core.errors.exception.Exception400;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * User 관련 비즈니스 로직
 * - 사용자의 프로필 이미지를 파일로 직접 생성하는 코드를 작성해 보자.
 * - 역할 : 실제 파일을 시스템에 저장하고 삭제하는 작업만 처리할 예정
 * - 주의 : 데이터베이스 업데이트는 UserService 에서 처리할 예정
 */
@Component
public class ImageUpload {

    // 프로필 이미지 파일 경로
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 이미지 정보 관련 파일 경로
    @Value("${file.image-upload-dir}")
    private String imageUploadDir;

    /**
     * 프로필 이미지 파일을 서버에 업로드 하는 메서드
     */
    public String uploadImage(MultipartFile multipartFile, String type) throws IOException {

        // 요청 타입(profile,imageInfo)에 따라 dir 설정
        String dir;
        // 요청 타입(profile,imageInfo)에 따라 application.yml에 세팅된 값의 Dir 세팅
        String setDestinationDir;

        if (type.equals("profile")){
            dir = "/uploads/profiles/";
            setDestinationDir = uploadDir;
        } else {
            dir = "/uploads/images/";
            setDestinationDir = imageUploadDir;
        }

        createUploadDirectory(setDestinationDir);

        String originalFilename = multipartFile.getOriginalFilename();

        String extension = getFileExtension(originalFilename);

        String uniqueFileName = generateUniqueFileName(extension);

        Path filePath;

        if (type.equals("profile")){
            filePath = Paths.get(uploadDir, uniqueFileName);
        } else {
            filePath = Paths.get(imageUploadDir, uniqueFileName);
        }

        multipartFile.transferTo(filePath);

        return dir + uniqueFileName;
    }

    // 고유한 파일명 생성
    private String generateUniqueFileName(String extension) {
        // 1. 현재 날짜와 시간을 "YYYMMDD_HHmmss" 형태로 포맷
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        // 2. UUID(범용 고유 식별자)를 생성하고 앞의 8자리만 사용
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        // 3. 최종 결과 : 20250721143022_1232acaadf.jpg
        return  timestamp + "_" + uuid +  extension;
    }

    // 파일 확장자면 추출해 주는 메서드
    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
            return ""; // 확장자가 없으면 빈 문자열을 반환
        }
        // 마지막 점(.) 문자 이후의 문자열ㅇ르 확장자로 반환
        // profile.jpg --> lastIndexOf(".") --> 7 반환
        return originalFilename.substring(originalFilename.lastIndexOf("."));
        // profile.jpg <---  .jpg <-- 이부분 추출 됨
    }

    // 폴더를 생성하는 메서드
    private void createUploadDirectory(String uploadDir) throws  IOException {
        // window, linux
        Path uploadPath = Paths.get(uploadDir);

        // 디렉토리가 존재 하지 않으면 생성
        // C:/uploads/profiles/ 경로가 없으면
        if(Files.exists(uploadPath) == false) {
            // 여러 레벨의 디렉토리를 한번에 생성해 준다.
            Files.createDirectories(uploadPath);
        }
    }

    //
    public void deleteImage(String imagePath, String type) {

        if (imagePath != null && imagePath.isEmpty() == false) {
            try {
                // updloads/profiles/1231241231.jpg
                // 1단계 : 전체 경로에서 파일명만 추출
                String fileName = imagePath.substring(imagePath.lastIndexOf("/")+1);
                // 2단계 : 실제 파일 시스템 경로 생성

                Path filePath;
                if (type.equals("profile")){
                    filePath = Paths.get(uploadDir, fileName);
                } else {
                    filePath = Paths.get(imageUploadDir, fileName);
                }

                // 3단계 : 파일이 존재하면 삭제, 없으면 아무것도 안함
                Files.deleteIfExists(filePath);

            } catch (IOException e) {
                throw new Exception400("프로필 이미지를 삭제 하지 못하였습니다.");
            }
        }
    }

}