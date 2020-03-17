package com.qf.controller;

import com.qf.dto.IndexBean;
import com.qf.entity.TCommodity;
import com.qf.entity.TProductType;
import com.qf.mapper.CommodityMapper;
import com.qf.service.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    public IHomePageService homePageService;



    @RequestMapping("index")
    @ResponseBody
    public IndexBean index(){
        IndexBean indexBean = homePageService.selectIndexAll();
        return indexBean;
    }
}
