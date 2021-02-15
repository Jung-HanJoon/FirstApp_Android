package com.example.rentaldream.model;

public class PostInfo implements Comparable<PostInfo>{
    private String title; //제목
    private String contents; //내용
    private String postuserID; //작성자 uid
    private String postuserName; //작성자 이름
    private String category; //카테고리
    private String product; //상품이름
    private long price1; //보증금
    private long price2; //대여료
    private String period; //기간
    private String yorn; //거래상태
    private String postDate; //작성시간
    private int booked; //찜횟수
    private int countView; //조회수
    private int extension; //
    private String imageURL;
    private String tradeWay;
    private String ordertype;
    private String postId;
    private String buyer;
    private long returndate;




    public PostInfo(){

    }

    public PostInfo(String title, String contents, String postuserID, String postuserName, String category, String product, long price1, long price2, String period, String yorn, String postDate, int booked, int countView, int extension, String imageURL, String tradeWay, String buyer, long returndate) {

        this.title = title;
        this.contents = contents;
        this.postuserID = postuserID;
        this.postuserName = postuserName;
        this.category = category;
        this.product = product;
        this.price1 = price1;
        this.price2 = price2;
        this.period = period;
        this.yorn = yorn;
        this.postDate = postDate;
        this.booked = booked;
        this.countView = countView;
        this.extension = extension;
        this.imageURL = imageURL;
        this.tradeWay = tradeWay;
        this.buyer = buyer;
        this.returndate =returndate;
    }

    public PostInfo(PostInfo toObject) {

        this.title = toObject.title;
        this.contents = toObject.contents;
        this.postuserID = toObject.postuserID;
        this.postuserName = toObject.postuserName;
        this.category = toObject.category;
        this.product = toObject.product;
        this.price1 = toObject.price1;
        this.price2 = toObject.price2;
        this.period = toObject.period;
        this.yorn = toObject.yorn;
        this.postDate = toObject.postDate;
        this.booked = toObject.booked;
        this.countView = toObject.countView;
        this.extension = toObject.extension;
        this.imageURL = toObject.imageURL;
        this.tradeWay = toObject.tradeWay;
        this.buyer = toObject.buyer;
        this.returndate = toObject.returndate;
    }

    public PostInfo(PostInfo toObject, String postId) {

        this.title = toObject.title;
        this.contents = toObject.contents;
        this.postuserID = toObject.postuserID;
        this.postuserName = toObject.postuserName;
        this.category = toObject.category;
        this.product = toObject.product;
        this.price1 = toObject.price1;
        this.price2 = toObject.price2;
        this.period = toObject.period;
        this.yorn = toObject.yorn;
        this.postDate = toObject.postDate;
        this.booked = toObject.booked;
        this.countView = toObject.countView;
        this.extension = toObject.extension;
        this.imageURL = toObject.imageURL;
        this.tradeWay = toObject.tradeWay;
        this.postId = postId;
        this.buyer= toObject.buyer;
        this.returndate = toObject.returndate;
    }

    public PostInfo(PostInfo toObject, String postId, String ordertype) {

        this.title = toObject.title;
        this.contents = toObject.contents;
        this.postuserID = toObject.postuserID;
        this.postuserName = toObject.postuserName;
        this.category = toObject.category;
        this.product = toObject.product;
        this.price1 = toObject.price1;
        this.price2 = toObject.price2;
        this.period = toObject.period;
        this.yorn = toObject.yorn;
        this.postDate = toObject.postDate;
        this.booked = toObject.booked;
        this.countView = toObject.countView;
        this.extension = toObject.extension;
        this.imageURL = toObject.imageURL;
        this.tradeWay = toObject.tradeWay;
        this.postId = postId;
        this.ordertype = ordertype;
        this.buyer=toObject.buyer;

    }

    public long getReturndate() {
        return returndate;
    }

    public void setReturndate(long returndate) {
        this.returndate = returndate;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getTradeWay() {
        return tradeWay;
    }

    public void setTradeWay(String tradeWay) {
        this.tradeWay = tradeWay;
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPostuserID() {
        return postuserID;
    }

    public void setPostuserID(String postuserID) {
        this.postuserID = postuserID;
    }

    public String getPostuserName() {
        return postuserName;
    }

    public void setPostuserName(String postuserName) {
        this.postuserName = postuserName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public long getPrice1() {
        return price1;
    }

    public void setPrice1(long price1) {
        this.price1 = price1;
    }

    public long getPrice2() {
        return price2;
    }

    public void setPrice2(long price2) {
        this.price2 = price2;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getYorn() {
        return yorn;
    }

    public void setYorn(String yorn) {
        this.yorn = yorn;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    public int getExtension() {
        return extension;
    }

    public void setExtension(int extension) {
        this.extension = extension;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }




    @Override
    public int compareTo(PostInfo o) {
        if(this.ordertype=="date"){
            return o.postDate.compareTo(this.postDate);
        }else if(this.ordertype=="book"){
            return String.valueOf(o.booked).compareTo(String.valueOf(this.booked));
        }
        return 0;
    }
}
