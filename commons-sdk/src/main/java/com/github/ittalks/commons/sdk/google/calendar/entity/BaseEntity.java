package com.github.ittalks.commons.sdk.google.calendar.entity;

/**
 * Created by zhaoyudong on 2017/3/7.
 */
public class BaseEntity {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * google账号
     */
    private String googleAccount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoogleAccount() {
        return googleAccount;
    }

    public void setGoogleAccount(String googleAccount) {
        this.googleAccount = googleAccount;
    }
}
