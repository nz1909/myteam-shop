package com.qf.interceptor;




import com.qf.constant.CookieConstant;
import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.entity.TUser;
import com.qf.util.StringUtil;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor1 implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //1.获取cookie中uuid

        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(CookieConstant.USER_LOGIN.equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    String redisKey = StringUtil.getRedisKey(RedisConstant.USER_LOGIN_PRE, uuid);
                    Object o = redisTemplate.opsForValue().get(redisKey);
                    if(o!=null){
                        //用户已登录
                        JSONObject jsonObject=JSONObject.fromObject(o); // 将数据转成json字符串
                        TUser user = (TUser) JSONObject.toBean(jsonObject, TUser.class);//将json转成需要的对象
                        //存入到request域中
                        request.setAttribute("user",user);

                        return true;
                    }

                }
            }
        }

        return true;
    }

}
