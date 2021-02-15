package com.example.rentaldream.model;

public class CommentInfo {

    private String postUid; // 게시글
    private String userUid; // 코맨트 작성자
    private String userName; //작성자
    private String ImageURL; // 코맨트 작성자 프로필사진 링크
    private String contents ; // 내용
    private String postDate ; // 작성시간




    public CommentInfo() {}

    public CommentInfo(String postUid, String userUid, String userName, String ImageURL, String contents, String postDate){
        this.postUid = postUid;
        this.userUid = userUid;
        this.userName = userName;
        this.ImageURL = ImageURL;
        this.contents = contents;
        this.postDate = postDate;
    }

    public CommentInfo(CommentInfo object) {
        this.postUid = object.postUid;
        this.userUid = object.userUid;
        this.userName = object.userName;
        this.ImageURL = object.ImageURL;
        this.contents = object.contents;
        this.postDate = object.postDate;
    }

    /*
        public CommentInfo(CommentInfo object){
            this.postUid = object.postUid;
            this.userUid = object.userUid;
            this.userName = object.userName;
            this.ImageURL = object.ImageURL;
            this.contents = object.contents;
            this.postDate = object.postDate;
        }

     */
/*
    public CommentInfo(Object comment){
        this.postUid = postUid;
        this.userUid = userUid;
        this.userName = userName;
        this.ImageURL = ImageURL;
        this.contents = contents;
        this.postDate = postDate;
    }
*/
    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
