package com.qf.mapper;

import com.qf.entity.TProductType;

import java.util.List;

public interface IndexMapper {
        //查询首页信息
        List<TProductType> selectAllQuery();
}
