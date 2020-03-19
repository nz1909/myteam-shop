package com.qf.controller;

import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.dto.TProductCartDTO;
import com.qf.entity.TOrder;
import com.qf.entity.TUser;
import com.qf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 跳转到订单页面
     * @return
     */
    @RequestMapping("orderConfirm")
    public String orderConfirm(Model model){
        List<Integer> list=new ArrayList<>();
        list.add(1);
        model.addAttribute("list",list);
        return "orderConfirm";
    }
        @RequestMapping("orders")
        @ResponseBody
        public ResultBean Orders(String s , Model model, HttpServletRequest request, HttpServletResponse response){
            String productKeys = StringUtil.getRedisKey(RedisConstant.PRODUCT_PRE, "sp");
            List<TProductCartDTO> products = (List<TProductCartDTO>) redisTemplate.opsForValue().get(productKeys);
            List<TProductCartDTO> products1 =new ArrayList<>();
                    String[] split = s.split(",");
            for (int i = 0; i <split.length ; i++) {
                for (int j = 0; j <products.size() ; j++) {
                    if (products.get(j).getCommodity().getCoId()==Integer.parseInt(split[i])){
                        products1.add(products.get(i));
                    }
                }
            }
            redisTemplate.opsForValue().set(productKeys,products1);
            oo(model);
            return ResultBean.success();
        }




        @RequestMapping("oo")
        public String oo(Model model){
            String productKeys = StringUtil.getRedisKey(RedisConstant.PRODUCT_PRE, "sp");
            List<TProductCartDTO> lists = (List<TProductCartDTO>) redisTemplate.opsForValue().get(productKeys);
            model.addAttribute("lists",lists);
            return "orderConfirm";
        }



    @RequestMapping("pay")
    public String pay(HttpServletRequest request){
        Object user = request.getAttribute("user");
        if (user==null){
            return "当前用户没有登录";
        }
//        从Redis中获取用户购买的商品列表
        String productKeys = StringUtil.getRedisKey(RedisConstant.PRODUCT_PRE, "sp");
        List<TProductCartDTO> lists = (List<TProductCartDTO>) redisTemplate.opsForValue().get(productKeys);

//        检测商品是否都有库存,如果没有库存需要提醒用户
        for (int i = 0; i <lists.size() ; i++) {
            if (lists.get(i).getCount()>lists.get(i).getCommodity().getStock()){
                return lists.get(i).getCommodity().getCoName()+"库存不足";
            }
        }
        int sumPrice=0;
        for (int i = 0; i <lists.size() ; i++) {
            sumPrice+=(lists.get(i).getCount()*lists.get(i).getPrice());
        }

//         创建订单对象
        TOrder order = new TOrder();
        order.setSumPrice(sumPrice);
        TUser user1= (TUser) user;
        order.setAccount(user1.getUname());
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        order.setCreatedate(time);
        order.setSite(1);
        order.setModePayment("支付宝");
        order.setStatus(0);
        return "";
    }



        //TODO 创建订单并插入到数据库,减库存，使用mq的分布式事务解决方案
        //Order orderData=orderService.createOrder(order, orderdetailList);

        //TODO 清空购物车


}
