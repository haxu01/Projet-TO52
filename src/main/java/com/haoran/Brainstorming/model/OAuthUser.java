package com.haoran.Brainstorming.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;


@TableName("oauth_user")
public class OAuthUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    // identifiant du compte oauth
    private Integer oauthId;
    @TableField("`type`")
    private String type;
    // Le nom de connexion du compte oauth
    private String login;
    @JsonIgnore
    private String accessToken;
    private Date inTime;
    // Profil personnel
    private String bio;
    private String email;
    // identifiant d'utilisateur local
    private Integer userId;
    private String refreshToken;
    private String unionId;
    // Le délai d'expiration du jeton, ici, est stocké dans String, et vous pouvez le convertir lorsque vous l'utilisez
    // Pourquoi utiliser String, car String peut être facilement converti en d'autres types..
    private String expiresIn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOauthId() {
        return oauthId;
    }

    public void setOauthId(Integer oauthId) {
        this.oauthId = oauthId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
