package edu.neu.covidcareapp.activities.community;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    public String username;
    public String title;
    public String description;
    public String postContext;
    private Object timeStamp ;

    public Post(String username, String title, String description, String postContext) {
        this.username = username;
        this.title = title;
        this.description = description;
        this.postContext = postContext;
        this.timeStamp = ServerValue.TIMESTAMP;
    }


    public Post() {
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getPostContext() {
        return postContext;
    }

    public void setPostContext(String postContext) {
        this.postContext = postContext;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }



}
