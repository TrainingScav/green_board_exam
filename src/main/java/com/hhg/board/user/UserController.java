package com.hhg.board.user;

import com.hhg.board._core.utils.Define;
import com.hhg.board._core.utils.ImageUpload;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/join-form")
    public String joinForm() {

        return "user/joinForm";
    }

    @PostMapping("/join")
    public String joinUser(@Valid UserRequest.JoinDTO joinDTO, Errors errors) {

        try {
            userService.join(joinDTO);
            return "redirect:/login-form";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/login-form")
    public String loginForm() {

        return "user/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid UserRequest.LoginDTO loginDTO, HttpSession session) {

        User loginUser = userService.login(loginDTO);

        session.setAttribute(Define.SESSION_USER, loginUser);

        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login-form";
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        User user = (User) session.getAttribute(Define.SESSION_USER);

        User selectedUser = userService.findUserById(user.getUserId());

        model.addAttribute("userInfo",selectedUser);

        return "user/mypage";
    }


}
