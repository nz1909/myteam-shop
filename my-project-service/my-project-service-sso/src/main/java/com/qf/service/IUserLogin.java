package com.qf.service;

import com.qf.dto.ResultBean;

public interface IUserLogin {
        ResultBean selectLogin(String uname,String password);
}
