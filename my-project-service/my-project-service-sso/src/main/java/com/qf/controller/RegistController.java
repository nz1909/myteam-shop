package com.qf.controller;


import com.qf.dto.ResultBean;
import com.qf.entity.TUser;
import com.qf.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("email")
public class RegistController {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private TUserMapper tUserMapper;

//        @RequestMapping()
//        public ResultBean getCode(@PathVariable String uuid,String phone){
//
//            String s = redisTemplate.opsForValue().get(phone);
//            if (s.equals(uuid)){
//                return ResultBean.success("验证码发送成功");
//            }
//            return ResultBean.success("验证码发送失败");
//        }


    @RequestMapping("send/{phone}/{code}/{password}")
    @ResponseBody
    public ResultBean doRegist(@PathVariable String phone, @PathVariable String code,@PathVariable String password){
        String s = redisTemplate.opsForValue().get(phone);
           if (s.equals(code)){
                Date date = new Date();
               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               String time = formatter.format(date);
                TUser tUser = new TUser();
                tUser.setUname(phone);
                tUser.setPassword(password);
                tUser.setPhone(phone);
                tUser.setCreateTime(time);
                try {
                    tUserMapper.insert(tUser);
                }catch (Exception e){
                    e.printStackTrace();
                    return ResultBean.success("注册失败");
                }
            }

        return ResultBean.success("注册成功");
    }
}
