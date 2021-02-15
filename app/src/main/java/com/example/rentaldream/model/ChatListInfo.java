

package com.example.rentaldream.model;


import java.io.Serializable;

public class ChatListInfo implements Serializable {

    private String seller;
    private String buyer;
    private String seller_name;
    private String buyer_name;
    private long checkb;
    private long checks;
    private String postID;
    private String title;
    private String sellerImg;
    private String buyerImg;
    private String postImg;
    private String chatRoomId;
    private String state;


    public ChatListInfo(){

    }

    public ChatListInfo(String seller, String buyer, String seller_name, String buyer_name, long checkb, long checks, String postID, String title, String sellerImg, String buyerImg, String postImg, String chatRoomId, String state) {
        this.seller= seller;
        this.buyer= buyer;
        this.seller_name= seller_name.toString();
        this.buyer_name= buyer_name.toString();
        this.checkb=checkb;
        this.checks=checks;
        this.postID=postID;
        this.title=title;
        this.sellerImg=sellerImg;
        this.buyerImg=buyerImg;
        this.postImg=postImg;
        this.chatRoomId=chatRoomId;
        this.state=state;
    }

    public ChatListInfo(ChatListInfo object) {
        this.seller= object.seller;
        this.buyer= object.buyer;
        this.seller_name= object.seller_name.toString();
        this.buyer_name= object.buyer_name.toString();
        this.checkb=object.checkb;
        this.checks=object.checks;
        this.postID=object.postID;
        this.title=object.title;
        this.sellerImg=object.sellerImg;
        this.buyerImg=object.buyerImg;
        this.postImg=object.postImg;
        this.state=object.state;
    }

    public ChatListInfo(ChatListInfo object, String chatRoomId) {
        this.seller= object.seller;
        this.buyer= object.buyer;
        this.seller_name= object.seller_name.toString();
        this.buyer_name= object.buyer_name.toString();
        this.checkb=object.checkb;
        this.checks=object.checks;
        this.postID=object.postID;
        this.title=object.title;
        this.sellerImg=object.sellerImg;
        this.buyerImg=object.buyerImg;
        this.postImg=object.postImg;
        this.chatRoomId = chatRoomId;
        this.state=object.state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }


    public String getSeller_name() {
        return seller_name.toString();
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getBuyer_name() {
        return buyer_name.toString();
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public long getCheckb() {
        return checkb;
    }

    public void setCheckb(long checkb) {
        this.checkb = checkb;
    }

    public long getChecks() {
        return checks;
    }

    public void setChecks(long checks) {
        this.checks = checks;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellerImg() {
        return sellerImg;
    }

    public void setSellerImg(String sellerImg) {
        this.sellerImg = sellerImg;
    }

    public String getBuyerImg() {
        return buyerImg;
    }

    public void setBuyerImg(String buyerImg) {
        this.buyerImg = buyerImg;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }
}

