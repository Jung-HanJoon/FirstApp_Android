package com.example.rentaldream.model;

public class MemberInfo {
    private String name;
    private String phoneNum;
    private String birthDay;
    private String pushToken;
    private String imageURL;
    private String lastlogin;

    public MemberInfo() {}

    public MemberInfo(String name, String phoneNum, String birthDay, String pushToken, String imageURL, String lastlogin){
        this.name = name;
        this.phoneNum = phoneNum;
        this.birthDay = birthDay;
        this.pushToken = pushToken;
        this.imageURL = imageURL;
        this.lastlogin = lastlogin;
    }

    public MemberInfo(MemberInfo user){
        this.name = user.name;
        this.phoneNum = user.phoneNum;
        this. birthDay = user.birthDay;
        this.pushToken = user.pushToken;
        this.imageURL = user.imageURL;
        this.lastlogin = user.lastlogin;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getphoneNum(){
        return this.phoneNum;
    }
    public void setphoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

    public String getbirthDay(){
        return this.birthDay;
    }
    public void setbirthDay(String birthDay){
        this.birthDay = birthDay;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
