package com.haoran.Brainstorming.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = -5051120337175047163L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String telegramName;
    private String avatar;
    @JsonIgnore
    private String password;
    private String email;
    private String mobile;
    //website personel
    private String website;
    //Profil personnel
    private String bio;
    private Integer score;
    private Date inTime;
    @JsonIgnore
    private String token;
    private Boolean emailNotification;
    private Boolean active;


    public Boolean getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public String getTelegramName() {
        return telegramName;
    }

    public void setTelegramName(String telegramName) {
        this.telegramName = telegramName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", telegramName='" + telegramName + '\'' + ", "
                + "avatar='" + avatar + '\'' + ", password='" + password + '\'' + ", email='" + email + '\'' + ", mobile='" +
                mobile + '\'' + ", website='" + website + '\'' + ", bio='" + bio + '\'' + ", score=" + score + ", inTime=" +
                inTime + ", token='" + token + '\'' + ", emailNotification=" + emailNotification + ", active=" + active + '}';
    }
}
