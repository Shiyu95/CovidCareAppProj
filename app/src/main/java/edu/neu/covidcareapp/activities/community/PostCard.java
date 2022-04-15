package edu.neu.covidcareapp.activities.community;

import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.neu.covidcareapp.activities.community.ItemClickListener;

public class PostCard implements ItemClickListener {

    private final String postTitle;
    private final String postDesc;
    private final String postContent;
    private final String postKey;
    String postDate;


    //Constructor
    public PostCard(String postKey,String postTitle,String postDesc, String postContent) {
        this.postKey = postKey;
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.postContent = postContent;
        this.postDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

    }

    //Getters for the itemName and itemURL
    public String getPostTitle() {
        return postTitle;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public String getPostContent() { return postContent; }

    public String getPostKey() { return postKey; }


    public String getTimeStamp() {
        return postDate;
    }


    @Override
    public void onItemClick(int pos) {

    }

}
