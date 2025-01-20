package com.marParaiso.applications.usecase.impl;

import com.marParaiso.applications.dto.PaymentDTO;
import com.marParaiso.applications.ports.PaymentRepository;
import com.marParaiso.applications.usecase.OrderService;
import com.marParaiso.applications.usecase.PaymentService;
import com.marParaiso.domain.entity.Order;
import com.marParaiso.domain.entity.Payment;
import com.marParaiso.infrastructure.repository.JpaPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final JpaPaymentRepository jpaPaymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderService orderService,
                              JpaPaymentRepository jpaPaymentRepository) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.jpaPaymentRepository = jpaPaymentRepository;
    }

    @Override
    public PaymentDTO processPayment(PaymentDTO paymentDTO, String username) {
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus("SUCCESS");

        Order order = orderService.getOrderEntityById(paymentDTO.getOrderId());
        if (!order.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You cannot pay for this order");
        }

        order.setPayment("PAID");
        payment.setOrder(order);

        return mapToPaymentDTO(jpaPaymentRepository.save(payment));
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return jpaPaymentRepository.findAll().stream()
                .map(this::mapToPaymentDTO)
                .collect(Collectors.toList());
    }

    private PaymentDTO mapToPaymentDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .paymentMethod(payment.getPaymentMethod())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .orderId(payment.getOrder().getId())
                .build();
    }
}
