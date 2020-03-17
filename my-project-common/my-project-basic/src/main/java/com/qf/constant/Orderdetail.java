package com.qf.constant;

import java.io.Serializable;

public class Orderdetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private int orderID;
    private int productID;
    private String giftID;
    private String price;
    private int number;
    private String fee;// 配送费
    private String isComment;
    private String productName;
    private String total0;// 小计
    private String lowStocks;// n:库存不足；y:库存充足。默认n
    private String specInfo;//商品规格信息

    @Deprecated
    private int score;

    /**
     * 订单项是否存在商品库存不足
     */
    public static final String orderdetail_lowstocks_n = "n";// 不存在
    public static final String orderdetail_lowstocks_y = "y";// 库存不足

    /**
     * 订单项是否已评论
     */
    public static final String orderdetail_isComment_n = "n";// 未评论
    public static final String orderdetail_isComment_y = "y";// 已评论

    public void clear() {
        id = null;
        orderID = 0;
        productID = 0;
        giftID = null;

        price = null;
        number = 0;

        fee = null;
        isComment = null;
        productName = null;

        total0 = null;
        lowStocks = null;
        specInfo = null;
        score = 0;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotal0() {
        return total0;
    }

    public void setTotal0(String total0) {
        this.total0 = total0;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public String getLowStocks() {
        return lowStocks;
    }

    public void setLowStocks(String lowStocks) {
        this.lowStocks = lowStocks;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSpecInfo() {
        return specInfo;
    }

    public void setSpecInfo(String specInfo) {
        this.specInfo = specInfo;
    }

    public String getGiftID() {
        return giftID;
    }

    public void setGiftID(String giftID) {
        this.giftID = giftID;
    }

}
