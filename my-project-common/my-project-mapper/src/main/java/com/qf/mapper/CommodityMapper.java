package com.qf.mapper;

import com.qf.entity.TCommodity;

import java.util.List;

public interface CommodityMapper {
    List<TCommodity> selectQueryCommpdity();

    TCommodity selectQuery(Integer coId);
}
