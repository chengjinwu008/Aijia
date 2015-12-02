package com.cjq.aijia.domain;

import com.orm.SugarRecord;

/**
 * Created by CJQ on 2015/11/12.
 */
public class User extends SugarRecord<User>{
    String userName;
    String password;
    String mobile;
    String profile;
    String points;
    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User(String userName, String password, String mobile, String profile, String points, String userId) {
        this.userName = userName;
        this.password = password;
        this.mobile = mobile;
        this.profile = profile;
        this.points = points;
        this.userId = userId;
    }

    public User(String userName, String password, String mobile, String profile, String points) {
        this.userName = userName;
        this.password = password;
        this.mobile = mobile;
        this.profile = profile;
        this.points = points;
    }

    public User(String userName, String mobile, String points, String profile) {
        this.userName = userName;
        this.mobile = mobile;
        this.points = points;
        this.profile = profile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }
}
