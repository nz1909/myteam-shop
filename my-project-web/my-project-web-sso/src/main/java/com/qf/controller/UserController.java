package com.qf.controller;

import com.qf.constant.CookieConstant;

import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class UserController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping("showLogin")
    public String showLogin(){
        return "login";
    }

    @RequestMapping("checkLogin")
    public String checkLogin(String uname, String password, HttpServletResponse response,
                                 @CookieValue(name = CookieConstant.USER_CART,required = false)String userCartUuid){
        //开始验证用户名密码是否正确
        //调用service服务
        String url = String.format("http://email-service/user/login/%s/%s",uname,password);
        ResultBean template = restTemplate.getForObject(url, ResultBean.class);
        if (template.getErrno()==0){
            //登录成功
            //生成cookie
            String uuid = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(CookieConstant.USER_LOGIN,uuid);
            //组织键
            String key = StringUtil.getRedisKey(RedisConstant.USER_LOGIN_PRE, uuid);
            //把登录成功后的用户对象存入到redis中。
            redisTemplate.opsForValue().set(key,template.getData(),30, TimeUnit.DAYS);
            //cookie要发送给客户端
            cookie.setMaxAge(30*24*60*60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "redirect:http://localhost:7761/index";
        }
        return "login";
    }


}
