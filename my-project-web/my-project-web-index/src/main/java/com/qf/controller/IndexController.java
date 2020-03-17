package com.qf.controller;



import com.qf.constant.CookieConstant;


import com.qf.constant.RedisConstant;
import com.qf.dto.IndexBean;
import com.qf.dto.ResultBean;


import com.qf.entity.TCommodity;
import com.qf.entity.TProductType;
import com.qf.util.StringUtil;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class IndexController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate
            ;

    @RequestMapping("index")
    public String index(Model model){
        String url = String.format("http://index-service/index");
        IndexBean forObject = restTemplate.getForObject(url, IndexBean.class);
        List<TProductType> types = forObject.gettProductTypes();
        List<TCommodity> list = forObject.gettCommodities();
        model.addAttribute("list",list);
        model.addAttribute("types",types);
        return "index";
    }
    /**
     * 判断用户是否已登录http://localhost:7762/checkIsLogin
     */
    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = CookieConstant.USER_LOGIN,required = false) String uuid, HttpServletRequest request){
        //user_login
        //验证我当前是否已登录
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(CookieConstant.USER_LOGIN.equals(cookie.getName())){
                    //找到这个cookie
                    //拿到cookie的值，组织redis的键
                    String uuid1 = cookie.getValue();
                    String redisKey = StringUtil.getRedisKey(RedisConstant.USER_LOGIN_PRE, uuid1);
                    Object o = redisTemplate.opsForValue().get(redisKey);
                    if(o!=null){
                        return ResultBean.success(o,"用户已登录");
                    }
                }
            }
        }
        return ResultBean.error();

    }


    /**
     * 注销
     */
    @RequestMapping("logout")
    public String logout(@CookieValue(name=CookieConstant.USER_LOGIN,required = false) String uuid,
                             HttpServletResponse response,Model model){
        //1.redis中删除
        //uuid有可能是空的，注销成功
        if(uuid!=null&&!"".equals(uuid)){
            String redisKey = StringUtil.getRedisKey(RedisConstant.USER_LOGIN_PRE, uuid);
            redisTemplate.delete(redisKey);
        }
        //2.cookie要删除
        //cookie如何删除？
        //cookie的键  path 是一样的
        Cookie cookie = new Cookie(CookieConstant.USER_LOGIN,"");
        cookie.setMaxAge(0);//删除cookie
        cookie.setPath("/");
        cookie.setHttpOnly(true);//只有后端程序能访问，提高cookie的安全性
        response.addCookie(cookie);
        return index(model);
    }
    //返回到商品详情页面
    @RequestMapping("introduction")
    public String introduction(){

        return "introduction";
    }
}
