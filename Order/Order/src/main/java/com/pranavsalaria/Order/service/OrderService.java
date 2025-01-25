package com.pranavsalaria.Order.service;

import com.pranavsalaria.Order.model.entity.Order;
import com.pranavsalaria.Order.model.response.BasicRestResponse;

public interface OrderService {
    BasicRestResponse placeOrder(Order order);
}
