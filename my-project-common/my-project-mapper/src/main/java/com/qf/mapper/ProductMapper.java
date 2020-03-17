package com.qf.mapper;

import com.qf.entity.TProduct;

public interface ProductMapper {
    TProduct selectByPrimaryKey(Long productId);
}
