package com.haoran.Brainstorming.config.realm;

import com.haoran.Brainstorming.util.bcrypt.BCryptPasswordEncoder;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

public class MyCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String rawPassword = String.valueOf((char[]) token.getCredentials());
        String encodedPassword = String.valueOf(info.getCredentials());
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
