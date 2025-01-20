package com.marParaiso.applications.controller;

import com.marParaiso.applications.dto.PaymentDTO;
import com.marParaiso.applications.usecase.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public PaymentDTO processPayment(@RequestBody PaymentDTO paymentDTO, @AuthenticationPrincipal String username) {
        return paymentService.processPayment(paymentDTO, username);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }
}
