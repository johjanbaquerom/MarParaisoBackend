package com.marParaiso.applications.usecase;

import com.marParaiso.applications.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {

    PaymentDTO processPayment(PaymentDTO paymentDTO, String username); // Procesar un pago
    List<PaymentDTO> getAllPayments();
}
