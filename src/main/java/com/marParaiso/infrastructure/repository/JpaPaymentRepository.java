package com.marParaiso.infrastructure.repository;

import com.marParaiso.applications.ports.PaymentRepository;
import com.marParaiso.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends PaymentRepository, JpaRepository<Payment, Long> {
}

