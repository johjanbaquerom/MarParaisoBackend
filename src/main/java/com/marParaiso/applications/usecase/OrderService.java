package com.marParaiso.applications.usecase;

import com.marParaiso.applications.dto.OrderDTO;
import com.marParaiso.domain.entity.Order;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByUsername(String username); // Usuario: Ver sus órdenes
    OrderDTO createOrder(OrderDTO orderDTO, String username); // Usuario: Crear orden
    OrderDTO updateOrderStatus(Long orderId, String status);
    Order getOrderEntityById(Long id);
    void deleteOrderById(Long id);
}
