package com.pranavsalaria.Inventory.service;

import com.pranavsalaria.Inventory.model.entity.Order;
import com.pranavsalaria.Inventory.model.response.BasicRestResponse;

public interface ItemService {

    BasicRestResponse itemStock(Order order);

}
