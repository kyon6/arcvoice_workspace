package com.wanmei.arcvoice.model;

import com.talkray.arcvoice.UserState;

import java.io.Serializable;

/**
 * Created by liang on 14/11/4.
 */
public class Player implements Serializable {

    private String userId;

    private String userName;

    private String userAvatar;

    private UserState userState;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    @Override
    public String toString() {
        return "Player{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userState=" + userState +
                '}';
    }
}
