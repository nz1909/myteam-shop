package com.qf.service.impl;

import com.qf.dto.IndexBean;
import com.qf.entity.TCommodity;
import com.qf.entity.TProductType;
import com.qf.mapper.CommodityMapper;
import com.qf.mapper.IndexMapper;
import com.qf.service.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class HomePageService implements IHomePageService {
    @Autowired
    private IndexMapper indexMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public IndexBean selectIndexAll() {
        List<TProductType> query=null;
        List<TCommodity> list=null;
        String key1="myteam-shop-index-sort";
        String key2="myteam-shop-index-commodity";
        query = (List<TProductType>) redisTemplate.opsForValue().get(key1);
        list = (List<TCommodity>) redisTemplate.opsForValue().get(key2);
        if (query==null){
                     query = indexMapper.selectAllQuery();
                     list=commodityMapper.selectQueryCommpdity();
                    if (query!=null && !query.isEmpty()){
                        redisTemplate.opsForValue().set(key1,query);
                        redisTemplate.opsForValue().set(key2,list);
                    }else {
                        //如果是数据库中没有的数据，则二十秒之后删除
//                        stringRedisTemplate.boundValueOps(key).set(toJson,20, TimeUnit.SECONDS);
                        redisTemplate.opsForValue().set(key1,query,20, TimeUnit.SECONDS);
                    }
                }
        IndexBean indexBean = new IndexBean();
        indexBean.settCommodities(list);
        indexBean.settProductTypes(query);
        return indexBean;
    }
}
