package com.pranavsalaria.Order.controller;

import com.pranavsalaria.Order.model.entity.Order;
import com.pranavsalaria.Order.model.response.BasicRestResponse;
import com.pranavsalaria.Order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/postOrder")
    public BasicRestResponse placeOrder(
            @RequestBody Order order
    ) {
        return service.placeOrder(order);
    }

}
