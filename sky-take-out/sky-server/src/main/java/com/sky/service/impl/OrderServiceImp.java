package com.sky.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private UserMapper userMapper;


    @Transactional
    public OrderSubmitVO makeOrder(OrdersSubmitDTO ordersSubmitDTO){

        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = new AddressBook();
        addressBook.setId(ordersSubmitDTO.getAddressBookId());
        List<AddressBook> query = addressBookMapper.query(addressBook);
        if (query==null || query.size()==0){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.queryShoppingCartByUserId(userId);
        if (shoppingCarts==null || shoppingCarts.size()==0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        addressBook = query.get(0);
        String addre = addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail();
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addre);
        orders.setConsignee(addressBook.getConsignee());

        orderMapper.save(orders);

        List<OrderDetail> list = new ArrayList<>();
        for (ShoppingCart s : shoppingCarts){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(s, orderDetail);
            orderDetail.setOrderId(orders.getId());
            list.add(orderDetail);
        }

        orderDetailMapper.save(list);

        shoppingCartMapper.clean(userId);

        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderAmount(orders.getAmount());

        return orderSubmitVO;
    }

    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }


    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }


    public PageResult queryOrder(OrdersPageQueryDTO ordersPageQueryDTO){
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> query = orderMapper.query(ordersPageQueryDTO);
        List<OrderVO> list = new ArrayList<>();
        if (query != null && query.size()!=0){
            for (Orders o : query) {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(o, orderVO);
                orderVO.setOrderDetailList(orderDetailMapper.queryByOrderId(o.getId()));
                StringBuilder sb = new StringBuilder();
                for (OrderDetail orderDetail : orderVO.getOrderDetailList()){
                    sb.append(orderDetail.getName()).append("*").append(orderDetail.getNumber()).append(";");
                }
                orderVO.setOrderDishes(sb.toString());
                list.add(orderVO);
            }
        }
        return new PageResult(query.getTotal(), list);
    }


    public OrderVO getOrderDetail(Long id){
        List<Orders> orders = orderMapper.queryById(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders.get(0), orderVO);
        List<OrderDetail> orderDetails = orderDetailMapper.queryByOrderId(id);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    public void cancelOrder(Long id){
        List<Orders> orders = orderMapper.queryById(id);
        Orders orders1 = orders.get(0);
        if (orders1.getStatus() > 2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders2 = new Orders();
        orders2.setId(orders1.getId());

        if (orders1.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
/**
            weChatPayUtil.refund(
                    orders1.getNumber(), //商户订单号
                    orders1.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额
**/
            orders1.setPayStatus(Orders.REFUND);
        }

        orders2.setStatus(Orders.CANCELLED);
        orders2.setCancelReason("用户取消");
        orders2.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders2);
    }


    public void again(Long id){
        List<OrderDetail> orderDetails = orderDetailMapper.queryByOrderId(id);
        shoppingCartMapper.clean(BaseContext.getCurrentId());
        for (OrderDetail od : orderDetails){
            ShoppingCart s = new ShoppingCart();
            BeanUtils.copyProperties(od, s);
            s.setUserId(BaseContext.getCurrentId());
            s.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.save(s);
        }
    }


    public OrderStatisticsVO getNumber(){
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setConfirmed(orderMapper.queryByStatus(Orders.CONFIRMED));
        orderStatisticsVO.setDeliveryInProgress(orderMapper.queryByStatus(Orders.DELIVERY_IN_PROGRESS));
        orderStatisticsVO.setToBeConfirmed(orderMapper.queryByStatus(Orders.TO_BE_CONFIRMED));
        return orderStatisticsVO;
    }


    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO){
        Orders orders = new Orders();
        orders.setId(ordersConfirmDTO.getId());
        orders.setStatus(Orders.CONFIRMED);
        orderMapper.update(orders);
    }


    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO){
        List<Orders> orders = orderMapper.queryById(ordersRejectionDTO.getId());
        if (orders.get(0).getStatus().intValue() != Orders.TO_BE_CONFIRMED.intValue()){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders1 = new Orders();
        orders1.setId(ordersRejectionDTO.getId());
        orders1.setStatus(Orders.CANCELLED);
        orders1.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders1.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders1);
    }


    public void cancelOrder(OrdersCancelDTO ordersCancelDTO){
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelTime(LocalDateTime.now());
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orderMapper.update(orders);
    }

    public void deliveryOrder(Long id){
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(orders);
    }

    public void finishOrder(Long id){
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

}
