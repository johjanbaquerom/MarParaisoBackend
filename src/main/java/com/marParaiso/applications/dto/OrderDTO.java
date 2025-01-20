package com.marParaiso.applications.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private List<OrderItemDTO> orderItems;
    private String customerUsername;
    private String status;
    private String paymentStatus;
}
