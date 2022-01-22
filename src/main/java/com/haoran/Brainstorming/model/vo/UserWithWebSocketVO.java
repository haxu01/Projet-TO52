package com.haoran.Brainstorming.model.vo;


import java.io.Serializable;

public class UserWithWebSocketVO implements Serializable {

    private static final long serialVersionUID = -8327007303087296114L;
    private String username;
    private Integer userId;

    public UserWithWebSocketVO() {
    }

    public UserWithWebSocketVO(String username, Integer userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
