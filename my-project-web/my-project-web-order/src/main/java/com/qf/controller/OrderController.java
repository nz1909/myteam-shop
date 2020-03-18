package com.qf.controller;
import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.dto.TProductCartDTO;
import com.qf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 跳转到订单页面
     * @return
     */
    @RequestMapping("orderConfirm")
    public String orderConfirm(Model model){
        List<Integer> list=new ArrayList<>();
        list.add(1);
        model.addAttribute("list",list);
        return "orderConfirm";
    }
        @RequestMapping("orders")
        @ResponseBody
        public ResultBean Orders(String s ,Model model){
            String productKeys = StringUtil.getRedisKey(RedisConstant.PRODUCT_PRE, "sp");
            List<TProductCartDTO> products = (List<TProductCartDTO>) redisTemplate.opsForValue().get(productKeys);
            List<TProductCartDTO> products1 =new ArrayList<>();
                    String[] split = s.split(",");
            for (int i = 0; i <split.length ; i++) {
                for (int j = 0; j <products.size() ; j++) {
                    if (products.get(j).getCommodity().getCoId()==Integer.parseInt(split[i])){
                        products1.add(products.get(i));
                    }
                }
            }
            model.addAttribute("products1",products);

            return ResultBean.success();
        }

//    /**
//     * 点击 创建订单 按钮
//     * @param e
//     * @param model
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "pay", method = RequestMethod.POST)
//    public String pay(Order e, ModelMap model, HttpServletRequest request) throws Exception{
//        Object user = request.getAttribute("user");
//        return insertAndPay(e, model,user);
//    }
//
//    /**
//     * 创建订单，并跳转到支付页面让用户进行支付
//     *
//     * @return
//     * @throws Exception
//     */
//    private String insertAndPay(Order e, ModelMap model,Object user) throws Exception {
//
//
//        if (user == null ) {
//            return "当前用户没有登录";
//        }
//
//        //TODO 从Redis中获取用户购买的商品列表
//        CartInfo cartInfo = null;
//        if (cartInfo == null || cartInfo.getProductList().size() == 0) {
//            throw new NullPointerException("购物车中没有可支付的商品!");
//        }
//
//        //TODO 检测商品是否都有库存,如果没有库存需要提醒用户
//        //TODO 库存不足，则提示用户某些商品的库存不足，请重新选购
//        //TODO 获取配送方式
//
//
//        //创建订单对象
//        Order order = new Order();
//        order.setAccount(user.toString());
//        order.setQuantity(cartInfo.getProductList().size());
//        order.setStatus(Order.order_status_init);
//        order.setPaystatus(Order.order_paystatus_n);
//        order.setOtherRequirement(e.getOtherRequirement());//附加要求
//
//
//        //创建订单明细集合
//        List<Orderdetail> orderdetailList = new LinkedList<Orderdetail>();
//        for (int i = 0; i < cartInfo.getProductList().size(); i++) {
//            TCommodity product = cartInfo.getProductList().get(i);
//            Orderdetail orderdetail = new Orderdetail();
//            orderdetail.setProductID(product.getCoId().intValue());
//            orderdetail.setPrice(product.getCoPrice());//商品现价
//            orderdetail.setFee("0");//配送费
//            orderdetail.setProductName(product.getCoName());
//            orderdetail.setTotal0(String.valueOf(Double.valueOf(orderdetail.getPrice()) * orderdetail.getNumber()));//订单项小计
//
//            orderdetailList.add(orderdetail);
//        }
//        if(orderdetailList.size()==1){
//            order.setRemark(orderdetailList.get(0).getProductName());
//        }else{
//            order.setRemark("合并|"+orderdetailList.size()+"笔订单");
//        }
//        cartInfo.totalCacl();
//        //TODO
//        order.setExpressCode(null);//配送方式编码
//        order.setExpressName(null);//配送方式名称
//        order.setFee(String.valueOf(null));//订单配送费
//        order.setPtotal(cartInfo.getAmount());//订单商品总金额
//        order.setAmount(String.valueOf(Double.valueOf(cartInfo.getAmount())+Double.valueOf(order.getFee())));//订单总金额 = 内存订单总金额 + 总配送费
//        order.setAmountExchangeScore(cartInfo.getTotalExchangeScore());//订单总兑换积分。订单支付成功以后扣除
//
//        /**
//         * 对金额进行格式化，防止出现double型数字计算造成的益出。
//         */
//        order.setAmount(df.format(Double.valueOf(order.getAmount())));//订单总金额
//        order.setPtotal(df.format(Double.valueOf(order.getPtotal())));//订单商品总金额
//        order.setFee(df.format(Double.valueOf(order.getFee())));//订单总配送费
//
//        //TODO 封装配送地址信息
//
//
//        //TODO 创建订单并插入到数据库,减库存，使用mq的分布式事务解决方案
//        //Order orderData=orderService.createOrder(order, orderdetailList);
//
//        //TODO 清空购物车
//
//
//        return "redirect:/paygate/pay?orderId={1}&orderPayId={2}";
//    }
//
//    DecimalFormat df = new DecimalFormat("0.00");
}
