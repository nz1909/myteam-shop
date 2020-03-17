package com.qf.entity;


import java.io.Serializable;


public class TUser implements Serializable {

  private Integer id;
  private String uname;
  private String password;
  private String phone;
  private String email;
  private Integer flag;
  private String createTime;
  private Integer createUser;
  private String updateTime;
  private Integer updateUser;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUname() {
    return uname;
  }

  public void setUname(String uname) {
    this.uname = uname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public Integer getCreateUser() {
    return createUser;
  }

  public void setCreateUser(Integer createUser) {
    this.createUser = createUser;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(Integer updateUser) {
    this.updateUser = updateUser;
  }
}
