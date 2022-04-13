package edu.neu.covidcareapp.activities.community;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private String content, userId, userImg, userName;
    String timeStamp;


    public Comment() {
    }

    public Comment(String content, String userId, String userImg, String userName) {
        this.content = content;
        this.userId = userId;
        this.userImg = userImg;
        this.userName = userName;
        this.timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timeStamp = timeStamp;
    }
}
