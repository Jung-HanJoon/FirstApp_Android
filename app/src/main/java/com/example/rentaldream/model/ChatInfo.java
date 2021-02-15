package com.example.rentaldream.model;


public class ChatInfo {
    private String postID;
    private String sendUid;
    private String sendName;
    private String sendImg;
    private String targetUid;
    private String targetName;
    private String targetImg;
    private String msg;
    private long timestamp;


    public ChatInfo(){

    }

    public ChatInfo(String postID, String sendUid, String sendName, String sendImg, String targetUid, String targetName, String targetImg, String msg, long timestamp) {
        this.postID = postID;
        this.sendUid = sendUid;
        this.sendName = sendName;
        this.sendImg = sendImg;
        this.targetUid = targetUid;
        this.targetName = targetName;
        this.targetImg = targetImg;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public ChatInfo(ChatInfo object) {
        this.postID = object.postID;
        this.sendUid = object.sendUid;
        this.sendName = object.sendName;
        this.sendImg = object.sendImg;
        this.targetUid = object.targetUid;
        this.targetName = object.targetName;
        this.targetImg = object.targetImg;
        this.msg = object.msg;
        this.timestamp = object.timestamp;
    }


    public String getSendImg() {
        return sendImg;
    }

    public void setSendImg(String sendImg) {
        this.sendImg = sendImg;
    }

    public String getTargetImg() {
        return targetImg;
    }

    public void setTargetImg(String targetImg) {
        this.targetImg = targetImg;
    }

    public String getPostId() {
        return postID;
    }

    public void setPostId(String postID) {
        this.postID = postID;
    }

    public String getSendUid() {
        return sendUid;
    }

    public void setSendUid(String sendUid) {
        this.sendUid = sendUid;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(String targetUid) {
        this.targetUid = targetUid;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

