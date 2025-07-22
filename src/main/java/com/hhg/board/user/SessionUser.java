package com.hhg.board.user;

import lombok.Builder;
import lombok.Data;

@Data
public class SessionUser {

    private Long userId;
    private String userLoginId;
    private String username;
    private String userEmail;

    @Builder
    public SessionUser(Long id, String username, String email) {
        this.userId = id;
        this.username = username;
        this.userEmail = email;
    }

    public SessionUser(User user) {
        this.userId = user.getUserId();
        this.userLoginId = user.getUserLoginId();
        this.username = user.getUsername();
        this.userEmail = user.getUserEmail();
    }
}
