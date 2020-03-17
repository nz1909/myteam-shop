package com.qf.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;


import com.qf.dto.ResultBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Controller
public class RegistController {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("register")
    public String register(){
        return "register";
    }
    @RequestMapping("getCode")
    @ResponseBody
    public ResultBean getCode(String phone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FizKwntLq4MkvPunXMv", "unNbMwb7vkMymhZ8P39jZUnBEhiZw2");
        IAcsClient client = new DefaultAcsClient(profile);
        int newCode= (int) ((Math.random()*999999)+100);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "优当网");
        request.putQueryParameter("TemplateCode", "SMS_181867559");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+newCode+"\"}");
        String uuid =null;
        try {
             uuid = String.valueOf(newCode);
            redisTemplate.opsForValue().set(phone,uuid,1000, TimeUnit.SECONDS);
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            String message = jsonObject.getString("Message");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new ResultBean();
        }

    @RequestMapping("doRegist")
    @ResponseBody
    public ResultBean doRegist(String phone, String code , String password){
        //调用service服务
        String url = String.format("http://email-service/email/send/%s/%s/%s",phone,code,password);
        try {
            ResultBean forObject = restTemplate.getForObject(url, ResultBean.class);
            return forObject;
        }catch (Exception e){
            e.printStackTrace();
            ResultBean resultBean = new ResultBean();
            resultBean.setMessage("1");
            return resultBean;
        }
    }
    //跳转到登录界面
    @RequestMapping("logging")
    public String logging(){
        return "login";
    }
}
