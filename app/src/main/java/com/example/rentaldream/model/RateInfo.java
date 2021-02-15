package com.example.rentaldream.model;

public class RateInfo {

    private String target;
    private String targetname;
    private String poster;
    private String postername;
    private String posterImg;
    private String postID;
    private String content;
    private String date;
    private int rate;


    public RateInfo(){

    }


    public RateInfo(String target, String targetname, String poster, String postername, String posterImg, String postID, String content, String date, int rate) {
        this.target = target;
        this.targetname = targetname;
        this.poster = poster;
        this.postername = postername;
        this.posterImg = posterImg;
        this.postID = postID;
        this.content = content;
        this.date = date;
        this.rate = rate;
    }

    public RateInfo(RateInfo object){
        this.target = object.target;
        this.targetname = object.targetname;
        this.poster = object.poster;
        this.postername = object.postername;
        this.posterImg = object.posterImg;
        this.postID = object.postID;
        this.content = object.content;
        this.date = object.date;
        this.rate = object.rate;
    }

    public String getTarget() {
        return target;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetname() {
        return targetname;
    }

    public void setTargetname(String targetname) {
        this.targetname = targetname;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPostername() {
        return postername;
    }

    public void setPostername(String postername) {
        this.postername = postername;
    }

    public String getPosterImg() {
        return posterImg;
    }

    public void setPosterImg(String posterImg) {
        this.posterImg = posterImg;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
