package com.pranavsalaria.Inventory.service.impl;

import com.pranavsalaria.Inventory.config.MQConfig;
import com.pranavsalaria.Inventory.model.entity.Item;
import com.pranavsalaria.Inventory.model.entity.Order;
import com.pranavsalaria.Inventory.model.response.BasicRestResponse;
import com.pranavsalaria.Inventory.repository.ItemRepository;
import com.pranavsalaria.Inventory.service.ItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

//    @Override
    @RabbitListener(queues = MQConfig.QUEUE)
    public BasicRestResponse itemStock(Order order) {
        BasicRestResponse response = BasicRestResponse.builder().build();
        Optional<Item> optionalItem = itemRepository.findByItemNo(order.getItemNo());
        if(optionalItem.isEmpty()) response.setMessage("Item unavailable");
        else {
            if(optionalItem.get().getStock().equals(0)) response.setMessage("Item unavailable");
            else response.setMessage("Item available");
        }
        return response;
    }
}
