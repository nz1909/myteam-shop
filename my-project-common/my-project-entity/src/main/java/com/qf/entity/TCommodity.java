package com.qf.entity;


import java.io.Serializable;

public class TCommodity implements Serializable {

  private Integer coId;
  private String coName;
  private String coImages;
  private Integer coPrice;
  private Integer code;
  private  Integer stock;


  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public Integer getCoId() {
    return coId;
  }

  public void setCoId(Integer coId) {
    this.coId = coId;
  }

  public String getCoName() {
    return coName;
  }

  public void setCoName(String coName) {
    this.coName = coName;
  }

  public String getCoImages() {
    return coImages;
  }

  public void setCoImages(String coImages) {
    this.coImages = coImages;
  }

  public Integer getCoPrice() {
    return coPrice;
  }

  public void setCoPrice(Integer coPrice) {
    this.coPrice = coPrice;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }
}
