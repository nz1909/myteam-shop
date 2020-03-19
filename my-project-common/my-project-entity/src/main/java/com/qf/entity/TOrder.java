package com.qf.entity;


import java.io.Serializable;

public class TOrder  implements Serializable {

  private long id;
  private String account;
  private String createdate;
  private long status;
  private String modePayment;
  private long site;
  private long sumPrice;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }


  public String getCreatedate() {
    return createdate;
  }

  public void setCreatedate(String createdate) {
    this.createdate = createdate;
  }


  public long getStatus() {
    return status;
  }

  public void setStatus(long status) {
    this.status = status;
  }


  public String getModePayment() {
    return modePayment;
  }

  public void setModePayment(String modePayment) {
    this.modePayment = modePayment;
  }


  public long getSite() {
    return site;
  }

  public void setSite(long site) {
    this.site = site;
  }


  public long getSumPrice() {
    return sumPrice;
  }

  public void setSumPrice(long sumPrice) {
    this.sumPrice = sumPrice;
  }

}
