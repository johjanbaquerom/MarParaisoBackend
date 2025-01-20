package com.marParaiso.applications.controller;

import com.marParaiso.applications.dto.OrderDTO;
import com.marParaiso.applications.usecase.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER')")
    public List<OrderDTO> getUserOrders(@AuthenticationPrincipal String username) {
        return orderService.getOrdersByUsername(username);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal String username) {
        return orderService.createOrder(orderDTO, username);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        return orderService.updateOrderStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
