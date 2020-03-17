package com.qf.controller;

import com.qf.dto.ResultBean;
import com.qf.service.IUserLogin;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
public class UserLoginController {
    @Autowired
    private IUserLogin userLogin;

    @RequestMapping("login/{uname}/{password}")
    @ResponseBody
    public ResultBean userLogin(@PathVariable String uname,@PathVariable String password){
        ResultBean resultBean = userLogin.selectLogin(uname, password);
        return resultBean;
    }
}
