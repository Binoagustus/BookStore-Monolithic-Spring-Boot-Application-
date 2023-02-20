package com.bridgelabz.bookstore.service;

import java.util.List;

import com.bridgelabz.bookstore.DTO.OrderDTO;
import com.bridgelabz.bookstore.DTO.UserOrderView;
import com.bridgelabz.bookstore.model.OrderModel;

public interface IOrderService {

	OrderModel placeOrder(String token, OrderDTO orderDTO);

	List<UserOrderView> getOrders(String token);
}
