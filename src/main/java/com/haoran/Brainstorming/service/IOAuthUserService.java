package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.OAuthUser;

import java.util.List;


public interface IOAuthUserService {
    OAuthUser selectByTypeAndLogin(String type, String login);

    List<OAuthUser> selectByUserId(Integer userId);

    void addOAuthUser(Integer oauthId, String type, String login, String accessToken, String bio, String email, Integer
            userId, String refreshToken, String unionId, String openId);

    void update(OAuthUser oAuthUser);
}
