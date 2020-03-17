package com.qf.mapper;

import com.qf.entity.TUser;

public interface TUserMapper {
    int deleteByPrimaryKey(Long uid);

    void insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Long uid);

    TUser selectUser(String uname);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);
}