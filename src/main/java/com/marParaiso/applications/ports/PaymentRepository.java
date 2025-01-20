package com.marParaiso.applications.ports;

import com.marParaiso.domain.entity.Payment;

import java.util.List;

public interface PaymentRepository {

    List<Payment> findByPaymentStatus(String paymentStatus);
    List<Payment> findByOrder_Id(Long orderId);

}
