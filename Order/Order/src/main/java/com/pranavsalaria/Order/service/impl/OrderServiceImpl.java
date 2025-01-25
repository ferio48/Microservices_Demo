package com.pranavsalaria.Order.service.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.pranavsalaria.Order.config.MQConfig;
import com.pranavsalaria.Order.model.entity.Order;
import com.pranavsalaria.Order.model.response.BasicRestResponse;
import com.pranavsalaria.Order.repository.OrderRepository;
import com.pranavsalaria.Order.service.OrderService;
import com.rabbitmq.tools.json.JSONUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public BasicRestResponse placeOrder(Order order) {
        BasicRestResponse response = BasicRestResponse
                .builder()
                .content(order)
                .build();

        BasicRestResponse invResp = template.convertSendAndReceiveAsType(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, order, ParameterizedTypeReference.forType(BasicRestResponse.class));

        if(invResp != null && invResp.getMessage().equalsIgnoreCase("Item available")) {
            response.setMessage("Item placed successfully");
            response.setContent(orderRepository.save(order));
        } else {
            response.setMessage("Item unavailable");
        }

        return response;
    }
}
