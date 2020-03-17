package com.qf.vo;

import java.io.Serializable;
import java.util.Date;

public class CartItem implements Serializable {



   private Integer coId;
    private int count;
    private Date updateTime;

    public Integer getCoId() {
        return coId;
    }

    public void setCoId(Integer coId) {
        this.coId = coId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
