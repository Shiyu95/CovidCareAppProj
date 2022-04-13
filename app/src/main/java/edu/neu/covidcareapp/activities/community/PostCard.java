package edu.neu.covidcareapp.activities.community;

import edu.neu.covidcareapp.activities.community.ItemClickListener;

public class PostCard implements ItemClickListener {

    private final String postTitle;
    private final String postDesc;
    private final String postContent;
    private final String postKey;


    //Constructor
    public PostCard(String postKey, String postTitle, String postDesc, String postContent) {
        this.postKey = postKey;
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.postContent = postContent;

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


    @Override
    public void onItemClick(int pos) {

    }
}
