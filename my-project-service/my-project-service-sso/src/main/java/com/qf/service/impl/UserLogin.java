package com.qf.service.impl;

import com.qf.dto.ResultBean;
import com.qf.entity.TUser;
import com.qf.mapper.TUserMapper;
import com.qf.service.IUserLogin;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLogin implements IUserLogin {
    @Autowired
    private TUserMapper tUserMapper;
    @Override
    public ResultBean selectLogin(String uname, String password) {

        if (uname!=null && password !=null){
            TUser tUser = tUserMapper.selectUser(uname);
            String pword = tUser.getPassword();
            //将前台传过来的值进行MD5加密(盐为手机号)
            String md2Hex = DigestUtils.md2Hex(password + tUser.getPhone());
            if (md2Hex.equals(pword)){
                ResultBean resultBean = new ResultBean();
                resultBean.setData(tUser);
                resultBean.setErrno(0);
                return resultBean;
            }else {
                ResultBean resultBean = new ResultBean();
                resultBean.setErrno(1);
                return resultBean;
            }
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setErrno(1);
        return resultBean;
    }
}
