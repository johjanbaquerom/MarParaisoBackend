package com.marParaiso.infrastructure.repository;

import com.marParaiso.applications.ports.OrderRepository;
import com.marParaiso.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends OrderRepository, JpaRepository<Order, Long> {
}
