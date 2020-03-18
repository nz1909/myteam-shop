package com.qf.dto;

import com.qf.entity.TCommodity;


import java.io.Serializable;
import java.util.Date;

public class TProductCartDTO implements Serializable {

    private TCommodity commodity;


    public TCommodity getCommodity() {
        return commodity;
    }

    public void setCommodity(TCommodity commodity) {
        this.commodity = commodity;
    }

    private int count;

    private Date updateTime;

    private Integer sumPrice;


    private Integer price;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Integer sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
