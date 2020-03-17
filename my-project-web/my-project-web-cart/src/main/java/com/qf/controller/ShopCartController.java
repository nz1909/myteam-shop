package com.qf.controller;

import com.qf.constant.CookieConstant;
import com.qf.dto.ResultBean;
import com.qf.dto.TProductCartDTO;
import com.qf.entity.TCommodity;
import com.qf.entity.TUser;
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
import java.util.List;
import java.util.UUID;

@Controller
public class ShopCartController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("shopCart")
    public String shopCart(){
        return "shopcart";
    }

    //返回到商品详情页面
    @RequestMapping("introduction")
    public String introduction(Integer coId, Model model){
        List<TCommodity> o = (List<TCommodity>) redisTemplate.opsForValue().get("myteam-shop-index-commodity");
        TCommodity tCommodity = new TCommodity();
            for (TCommodity list:o) {
                if (list.getCoId().equals(coId)){
                   tCommodity.setCoId(coId);
                    tCommodity.setCoName(list.getCoName());
                    tCommodity.setCoImages(list.getCoImages());
                    tCommodity.setCoPrice(list.getCoPrice());
                }
        }
        model.addAttribute("lists",tCommodity);
        return "introduction";
    }
    @RequestMapping("cart")
    @ResponseBody
    public ResultBean cart(@CookieValue(name = CookieConstant.USER_CART, required = false) String uuid,
                           Integer coId,
                           int count,
                           HttpServletResponse response,
                           HttpServletRequest request){
        Object o = request.getAttribute("user");
        if(o!=null){
            //================已登录状态下的购物车======================= redis:    user:cart:userId
            TUser user = (TUser) o;
            Integer id = user.getId();
            String url = String.format("http://cart-service/addProduct/%s/%s/%s",id,coId,count);
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            return resultBean;

//            return cartService.addProduct(userId.toString(),productId,count);

        }


        //==============未登录状态下的购物车=================
        //把该商品添加到购物车，这个购物车是在redis中。

        if(uuid==null||"".equals(uuid)){
            //uuid为空的话再生成一个uuid放到cookie里给客户端
            uuid = UUID.randomUUID().toString();
            //返回该uuid给cookie
            Cookie cookie = new Cookie(CookieConstant.USER_CART,uuid);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
//        ResultBean resultBean = cartService.addProduct(uuid,productId,count);
        String url = String.format("http://cart-service/addProduct/%s/%s/%s",uuid,coId,count);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
//
        return resultBean;
        }

    /**
     * 合并两种状态下的购物车
     * @param uuid
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("merge")
    @ResponseBody
    public ResultBean merge(@CookieValue(name = CookieConstant.USER_CART,required = false)String uuid,
                            HttpServletRequest request,HttpServletResponse response){
        //获得uuid,和uid
        TUser user = (TUser) request.getAttribute("user");
        String userId = null;
        if(user!=null){
            userId = user.getId().toString();
        }


        //做完合并以后，要把未登录状态下的购物车清空。在清空cookie
        Cookie cookie = new Cookie(CookieConstant.USER_CART,"");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        String url = String.format("http://cart-service/merge/%s/%s",uuid,userId);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
//        return cartService.merge(uuid,userId);
        return resultBean;

    }

    /**
     * 清空购物车
     * @param uuid
     * @param response
     * @return
     */
    @RequestMapping("clean")
    @ResponseBody
    public ResultBean cleanCart(@CookieValue(name=CookieConstant.USER_CART,required = false)String uuid,HttpServletResponse response,
                                HttpServletRequest request){
        Object o = request.getAttribute("user");
        if(o!=null){
            //===========已登录状态下的购物车============
            TUser user = (TUser) o;
//            return cartService.clean(user.getId().toString());
            String url = String.format("http://cart-service/clean/%s",user.getId().toString());
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            return resultBean;
        }
        //===========未登录状态下的购物车===========
        if(uuid!=null&&!"".equals(uuid)){
            //删除Cookie
            Cookie cookie = new Cookie(CookieConstant.USER_CART,"");
            cookie.setMaxAge(0);
            cookie.setPath("/");//admin.qf.com  sso.qf.com  ****.qf.com
            // cookie.setDomain("qf.com");//如果我们使用域名来访问，那么cookie不会被携带，只有这边设置了这个一级域名，qf.com,那么在该域名下的所有二级cookie就都可以携带该cookie
            response.addCookie(cookie);

            //删除redis中的购物车
//            return cartService.clean(uuid);
            String id=uuid;
            String url = String.format("http://cart-service/clean/%s",id);
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            return resultBean;
        }
        return ResultBean.error("当前用户没有购物车");

    }

    /**
     * 更新购物车中的商品
     * @param
     * @param count
     * @param uuid
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultBean updateCart(
            Integer coId,
            int count,
            @CookieValue(name=CookieConstant.USER_CART,required = false)String uuid,
            HttpServletRequest request){
        Object o = request.getAttribute("user");
        if(o!=null){
            //=============已登录状态下的购物车==============  user:cart:userId
            TUser user = (TUser) o;
            Integer id = user.getId();

            String url = String.format("http://cart-service/update/%/%s/%s",id,coId,count);
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            return resultBean;
        }

        String id=uuid;
        String url = String.format("http://cart-service/update/%/%s/%s",id,coId,count);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
        return resultBean;

    }



    /**
     * 展示购物车
     * @param uuid
     * @param request
     * @return
     */
    @RequestMapping("show")
    public String showCart(@CookieValue(name=CookieConstant.USER_CART,required = false)String uuid,HttpServletRequest request,Model model,HttpServletResponse response){
        Object o = request.getAttribute("user");
        //============查看已登录状态下的购物车=============
        if(o!=null){
            ResultBean merge = merge(uuid, request, response);

            TUser user = (TUser) o;
            Integer userId = user.getId();
            String id = userId.toString();
            String url = String.format("http://cart-service/showCart/%s",id);
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            List<TProductCartDTO> data = (List<TProductCartDTO>) resultBean.getData();
            model.addAttribute("data",data);
            return "shopcart";
        }
        //============查看未登录状态下的购物车=============
//        return cartService.showCart(uuid);
        String id=uuid;
        String url = String.format("http://cart-service/showCart/%s",id);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
        List<TProductCartDTO> data = (List<TProductCartDTO>) resultBean.getData();
        model.addAttribute("data",data);
        return "shopcart";
    }

    /**
     * 根据id删除购物车中的商品
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer coId,@CookieValue(name=CookieConstant.USER_CART,required = false)String uuid,HttpServletRequest request,Model model,HttpServletResponse response){
        Object o = request.getAttribute("user");
        if (o!=null){
            TUser user = (TUser) o;
            Integer userId = user.getId();
            String id = userId.toString();
            String url = String.format("http://cart-service/deleteCart/%s/%s",id,coId);
            restTemplate.getForObject(url, ResultBean.class);
            return showCart(uuid,request,model,response);
        }
        String id=uuid;
        String url = String.format("http://cart-service/deleteCart/%s/%s",id,coId);
            restTemplate.getForObject(url, ResultBean.class);
        return showCart(uuid,request,model,response);
    }
}
