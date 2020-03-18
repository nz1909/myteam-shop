package com.qf.controller;

import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.dto.TProductCartDTO;
import com.qf.entity.TCommodity;

import com.qf.mapper.CommodityMapper;

import com.qf.util.StringUtil;
import com.qf.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.*;

@Controller
public class CartController {
    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("addProduct/{id}/{coId}/{count}")
    @ResponseBody
    public ResultBean addProduct(@PathVariable String id,@PathVariable Integer coId,@PathVariable Integer count){
        String redisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, id);//user:cart:uuid

        Object o = redisTemplate.opsForValue().get(redisKey);
        if(o==null){
            //当前用户没有购物车
            //封装购物车商品对象
            CartItem cartItem = new CartItem();
            cartItem.setCoId(coId);
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());

            //存入到购物车中
            List<CartItem> carts = new ArrayList<>();
            carts.add(cartItem);
            //存入到redis中
            redisTemplate.opsForValue().set(redisKey,carts);
            return ResultBean.success(carts,"添加购物车成功");
        }

        //第2 或第3中情况
        List<CartItem> carts = (List<CartItem>) o;
        for (CartItem cartItem : carts) {

            if(cartItem.getCoId().longValue()==coId.longValue()){
                //当前用户有购物车，且购物车中有该商品
                cartItem.setCount(cartItem.getCount()+count);
                //更新商品的时间
                cartItem.setUpdateTime(new Date());
                //购物车中的商品已更新，得把购物车存回到redis中
                redisTemplate.opsForValue().set(redisKey,carts);
                return ResultBean.success(carts,"添加购物车成功");
            }
        }

        //当前用户有购物车，但购物车中没有该商品
        //封装购物车商品对象
        CartItem cartItem = new CartItem();
        cartItem.setCoId(coId);
        cartItem.setCount(count);
        cartItem.setUpdateTime(new Date());
        carts.add(cartItem);
        //存到redis里面
        redisTemplate.opsForValue().set(redisKey,carts);
        return ResultBean.success(carts,"添加购物车成功");
    }

    //合并购物车
    @RequestMapping("merge/{uuid}/{userId}")
    @ResponseBody
    public ResultBean merge(@PathVariable String uuid, @PathVariable String userId){
        //获得两种状态下的购物车
        String noLoginRedisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, uuid);
        String loginRedisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, userId);
        Object noLoginO = redisTemplate.opsForValue().get(noLoginRedisKey);//未登录下的购物车
        Object loginO = redisTemplate.opsForValue().get(loginRedisKey);//已登录下的购物车
        if(noLoginO==null){
            //1.未登录状态下没有购物车==》合并成功
            return ResultBean.success("未登录状态下没有购物车，不需要合并");
        }

        if(loginO==null){
            //2.未登录状态下有购物车，但已登录状态下没有购物车==》把未登录的变成已登录的
            redisTemplate.opsForValue().set(loginRedisKey,noLoginO);
            //删除未登录状态下的购物车
            redisTemplate.delete(noLoginRedisKey);
            return ResultBean.success(noLoginO,"合并成功");
        }

        //3.未登录状态下有购物车，但已登录状态下也有购物车，而且购物车中的商品有重复==》难点！
        List<CartItem> noLoginCarts = (List<CartItem>) noLoginO;
        List<CartItem> loginCarts = (List<CartItem>) loginO;
        //先创建一个Map
        Map<Integer,CartItem> map = new HashMap<>();

        for (int i = 0; i <noLoginCarts.size() ; i++) {
            map.put(noLoginCarts.get(i).getCoId(),noLoginCarts.get(i));
        }
        //此时map中就有所有的未登录状态下的购物车的商品
        //存入已登录状态下购物车的商品
        for (CartItem loginCartItem : loginCarts) {
            //尝试去检查下map中该商品是否已存在
            CartItem currentCartItem = map.get(loginCartItem.getCoId());
            if(currentCartItem!=null){
                //已存在
                currentCartItem.setCount(currentCartItem.getCount()+loginCartItem.getCount());
                //时间 必然是未登录状态下的更近
            }else{
                //不存在，直接放
                map.put(loginCartItem.getCoId(),loginCartItem);
            }
        }
        //此时Map中存放的数据就是合并之后的购物车
        //删除未登录状态下的购物车
        redisTemplate.delete(noLoginRedisKey);
        //把新的购物车存入到redis中
        Collection<CartItem> values = map.values();
        List<CartItem> newCarts = new ArrayList<>(values);
        redisTemplate.opsForValue().set(loginRedisKey,newCarts);
        return ResultBean.success(newCarts,"合并成功");

    }

    /**
     * 清空购物车
     * @param uuid
     * @return
     */
    @RequestMapping("clean/{uuid}")
    @ResponseBody
    public ResultBean clean(@PathVariable String uuid){
        String redisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, uuid);
        redisTemplate.delete(redisKey);
        return ResultBean.success("清空购物车成功");
    }

    /**
     * 更新购物车
     * @param uuid
     * @param
     * @param count
     * @return
     */
    @RequestMapping("update/{id}/{coId}/{count}")
    @ResponseBody
    public ResultBean update(@PathVariable String uuid,@PathVariable Integer coId,@PathVariable int count){
        if(uuid!=null&&!"".equals(uuid)) {
            //组织redis中的键
            String redisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, uuid);
            Object o = redisTemplate.opsForValue().get(redisKey);
            if (o != null) {
                List<CartItem> carts = (List<CartItem>) o;
                for (CartItem cartItem : carts) {
                    if (cartItem.getCoId().longValue() == coId.longValue()) {
                        cartItem.setCount(count);
                        cartItem.setUpdateTime(new Date());
                        //把集合直接存回到redis中
                        redisTemplate.opsForValue().set(redisKey, carts);
                        return ResultBean.success(carts, "更新购物车成功");
                    }

                }
            }
        }
        return ResultBean.error("当前用户没有购物车");
    }

    @RequestMapping("showCart/{id}")
    @ResponseBody
    public ResultBean showCart(@PathVariable String id){
        if(id!=null&&!"".equals(id)){
            String redisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, id);
            Object o = redisTemplate.opsForValue().get(redisKey);
            if(o!=null){
                List<CartItem> carts = (List<CartItem>) o;
//                List<TProduct> products = new ArrayList<>();
                List<TProductCartDTO> products = new ArrayList<>();
                for (CartItem cartItem : carts) {
                    //去reids中取
                    // product:10
                    String productKey = StringUtil.getRedisKey(RedisConstant.PRODUCT_PRE, cartItem.getCoId().toString());
                    //显示商品

                    TCommodity pro = (TCommodity) redisTemplate.opsForValue().get(productKey);
                    if(pro==null){
                        //去数据库拿。再存redis
                        pro = commodityMapper.selectQuery(cartItem.getCoId());
                        //存redis
                        redisTemplate.opsForValue().set(productKey,pro);
                    }
                    //pro肯定是有的
                    TProductCartDTO cartDTO = new TProductCartDTO();
                    //封装
                    cartDTO.setCommodity(pro);
                    cartDTO.setCount(cartItem.getCount());
                    cartDTO.setUpdateTime(cartItem.getUpdateTime());
                    //存到product集合中
                    Integer coPrice = pro.getCoPrice();
                    cartDTO.setPrice(coPrice);
                    products.add(cartDTO);

                }
                String productKeys = StringUtil.getRedisKey(RedisConstant.PRODUCT_PRE, "sp");
                redisTemplate.opsForValue().set(productKeys,products);
                //对集合中的元素进行排序，Comparator 用来指明排序依据。
                Collections.sort(products, new Comparator<TProductCartDTO>() {
                    @Override
                    public int compare(TProductCartDTO o2, TProductCartDTO o1) {
                        return (int)(o1.getUpdateTime().getTime()-o2.getUpdateTime().getTime());
                    }
                });

                return ResultBean.success(products);
            }
        }
        return ResultBean.error("当前用户没有购物车");
    }
    @RequestMapping("deleteCart/{id}")
    @ResponseBody
    public ResultBean deleteCart(@PathVariable String id,@PathVariable Integer coId){
        String loginRedisKey = StringUtil.getRedisKey(RedisConstant.USER_CART_PRE, id);
        Object os = redisTemplate.opsForValue().get(loginRedisKey);       //获取到购物车
        List<CartItem> noyrs = (List<CartItem>) os;
        for (CartItem list:noyrs) {
            if (list.getCoId()==coId){
               noyrs.remove(list);
            }
        }
        redisTemplate.opsForValue().set(loginRedisKey,noyrs);
        return new ResultBean();
    }
}
