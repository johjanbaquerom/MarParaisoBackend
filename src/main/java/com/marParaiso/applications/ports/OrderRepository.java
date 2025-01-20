package com.marParaiso.applications.ports;

import com.marParaiso.domain.entity.Order;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<Order> findAll();
    Optional<Order> findById(Long id);
    Order save(Order order);
    List<Order> findByUserUsername(String username);
    List<Order> findByStatus(String status);
    boolean existsById(@Param("id") Long id);
    void deleteById(@Param("id") Long id);
}
