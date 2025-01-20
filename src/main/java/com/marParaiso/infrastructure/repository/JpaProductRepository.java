package com.marParaiso.infrastructure.repository;

import com.marParaiso.applications.ports.ProductRepository;
import com.marParaiso.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository  extends ProductRepository, JpaRepository<Product, Long> {

}
