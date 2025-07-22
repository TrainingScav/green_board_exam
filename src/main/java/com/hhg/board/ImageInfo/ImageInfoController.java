package com.hhg.board.ImageInfo;

import com.hhg.board._core.errors.exception.Exception500;
import com.hhg.board._core.utils.Define;
import com.hhg.board._core.utils.ImageUpload;
import com.hhg.board.user.User;
import com.hhg.board.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class ImageInfoController {

    private final ImageUpload imageUploadService;
    private final ImageInfoService imageInfoService;
    private final UserService userService;

    // 메인 화면 이동
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 이미지 정보 목록 화면 이동
    @GetMapping("/image-infos")
    public String imageInfoList(Model model) {

        List<ImageInfo> imageInfoList = imageInfoService.getImageInfoList();

        model.addAttribute("imageInfoList", imageInfoList);

        return "imageInfo/imageInfoList";
    }

    // 이미지 정보 상세 화면 이동
    @GetMapping("/image-infos/{id}")
    public String imageInfoDetail(@PathVariable(name = "id")Long ImageInfoId, HttpSession session, Model model) {

        User sessionUser = (User)session.getAttribute(Define.SESSION_USER);

        ImageInfo imageInfo = imageInfoService.getImageInfo(ImageInfoId);

        if(sessionUser != null) {
            boolean isBoardOwner = imageInfo.isOwner(sessionUser.getUserId());
            imageInfo.setImageInfoOwner(isBoardOwner);
        }

        model.addAttribute("imageInfo", imageInfo);

        return "imageInfo/imageInfoDetail";
    }

    // 이미지 정보 수정 화면 이동
    @GetMapping("/image-infos/{id}/update")
    public String imageInfoUpdateForm(@PathVariable(name = "id")Long ImageInfoId, HttpSession session, Model model) {

        ImageInfo imageInfo = imageInfoService.getImageInfo(ImageInfoId);

        model.addAttribute("imageInfo", imageInfo);

        return "imageInfo/imageInfoUpdateForm";
    }

    // 이미지 수정 처리
    @PostMapping("/image-infos/{id}/update")
    public String imageInfoUpdate(@PathVariable(name = "id")Long imageInfoId, @Valid ImageInfoRequest.UpdateImageInfoDTO updateImageInfoDTO, Errors errors, HttpSession session, Model model) {

        User user = (User) session.getAttribute(Define.SESSION_USER);

        try {
            imageInfoService.updateImageInfo(imageInfoId, user, updateImageInfoDTO);
            return "redirect:/image-infos/"+imageInfoId+"/update";
        } catch (Exception e) {
            throw new Exception500("이미지 정보 등록 처리 중 오류가 발생 했습니다. 관리자에게 문의 하세요.");
        }
    }

    // 이미지 정보 삭제
    @PostMapping("/image-infos/{id}/delete")
    public String imageInfoDelete(@PathVariable(name = "id")Long ImageInfoId, HttpSession session, Model model) {

        imageInfoService.deleteImageInfo(ImageInfoId);

        return "redirect:/image-infos";
    }

    // 이미지 정보 등록 폼 이동
    @GetMapping("/image-infos/form")
    public String imageInfoForm() {
        return "imageInfo/imageInfoForm";
    }

    // 이미지 정보 등록
    @PostMapping("/image-infos")
    public String saveImageInfo(@Valid ImageInfoRequest.SaveImageInfoDTO saveImageInfoDTO, Errors errors, HttpSession sessionUser) {

        User user = (User) sessionUser.getAttribute(Define.SESSION_USER);

        try {

            User selectedUser = userService.findUserById(user.getUserId());

            String imgDir = imageUploadService.uploadImage(saveImageInfoDTO.getImageFile(), "imageInfo");
            saveImageInfoDTO.setImagePath(imgDir);

            imageInfoService.saveImageInfo(saveImageInfoDTO.toEntity(selectedUser));

            return "imageInfo/imageInfoForm";
        } catch (Exception e) {
            throw new Exception500("이미지 정보 등록 처리 중 오류가 발생 했습니다. 관리자에게 문의 하세요.");
        }
    }
}
