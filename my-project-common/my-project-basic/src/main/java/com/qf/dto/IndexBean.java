package com.qf.dto;

import com.qf.entity.TCommodity;
import com.qf.entity.TProductType;

import java.io.Serializable;
import java.util.List;

public class IndexBean implements Serializable {
    private List<TProductType> tProductTypes;

    private  List<TCommodity> tCommodities;

    public List<TProductType> gettProductTypes() {
        return tProductTypes;
    }

    public void settProductTypes(List<TProductType> tProductTypes) {
        this.tProductTypes = tProductTypes;
    }

    public List<TCommodity> gettCommodities() {
        return tCommodities;
    }

    public void settCommodities(List<TCommodity> tCommodities) {
        this.tCommodities = tCommodities;
    }
}
