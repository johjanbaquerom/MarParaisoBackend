package com.marParaiso.applications.usecase.impl;

import com.marParaiso.applications.dto.OrderDTO;
import com.marParaiso.applications.dto.OrderItemDTO;
import com.marParaiso.applications.ports.OrderRepository;
import com.marParaiso.applications.ports.ProductRepository;
import com.marParaiso.applications.ports.UserRepository;
import com.marParaiso.applications.usecase.OrderService;
import com.marParaiso.applications.usecase.ProductService;
import com.marParaiso.domain.entity.Order;
import com.marParaiso.domain.entity.OrderItem;
import com.marParaiso.domain.entity.Product;
import com.marParaiso.domain.entity.User;
import com.marParaiso.domain.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService,
                            UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        orders.forEach(order -> Hibernate.initialize(order.getOrderItems()));

        return orders.stream()
                .map(this::mapToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUsername(String username) {
        List<Order> orders = orderRepository.findByUserUsername(username);

        orders.forEach(order -> Hibernate.initialize(order.getOrderItems()));

        return orders.stream()
                .map(this::mapToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO, String username) {

        if (orderDTO.getOrderItems() == null || orderDTO.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("The order must have at least one item.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .totalAmount(0.0)
                .build();

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                .map(itemDTO -> {
                    Product product = productService.getProductEntityById(itemDTO.getProductId());
                    productService.updateStock(itemDTO.getProductId(), itemDTO.getQuantity());

                    Double unitPrice = product.getPrice();
                    OrderItem orderItem = new OrderItem(product, itemDTO.getQuantity(), unitPrice, order);

                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(new HashSet<>(orderItems));

        Double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(status);

        return mapToOrderDTO(orderRepository.save(order));
    }

    @Override
    public Order getOrderEntityById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id " + id));
    }

    @Override
    public void deleteOrderById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order with ID " + id + " not found");
        }
        orderRepository.deleteById(id);
    }

    private OrderDTO mapToOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .customerUsername(order.getUser().getUsername())
                .status(order.getStatus())
                .paymentStatus(order.getPayment())
                .orderItems(order.getOrderItems().stream()
                        .map(item -> new OrderItemDTO(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity()
                        ))
                        .collect(Collectors.toList()))
                .build();
    }
}
