package com.jfkj.im.Bean;

public class TUserBean {

    public TUserBean(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    private String userName ;

    private String userId;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
