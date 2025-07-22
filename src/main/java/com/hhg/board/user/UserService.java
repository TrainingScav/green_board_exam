package com.hhg.board.user;

import com.hhg.board._core.errors.exception.Exception401;
import com.hhg.board._core.errors.exception.Exception404;
import com.hhg.board._core.utils.ImageUpload;
import com.hhg.board._core.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ImageUpload imageUpload;
    private final UserJpaRepository userJpaRepository;


    public User findUserById(Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(() -> {
            throw new Exception404("해당 유저를 찾지 못헀습니다.");
        });

        return user;
    }

    @Transactional
    public void join(UserRequest.JoinDTO joinDTO) throws IOException {

        String imageDir = imageUpload.uploadImage(joinDTO.getProfileImage(), "profile");
        joinDTO.setProfileImagePath(imageDir);

        userJpaRepository.save(joinDTO.toEntity());
    }

    public User login(UserRequest.LoginDTO loginDTO) {
        User selectedUser = userJpaRepository.findByUsernameAndPassword(loginDTO.getUserLoginId(),loginDTO.getUserPassword())
                .orElseThrow(() -> {
                    throw new Exception401("사용자명 또는 비밀번호가 틀렸어요");
                });
        // JWT 발급해서 Controller 단으로 넘겨 주면 된다.
        //String jwt = JwtUtil.create(selectedUser);

        return selectedUser;
    }


}
