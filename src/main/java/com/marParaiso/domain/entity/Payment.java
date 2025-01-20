package com.marParaiso.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pago")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metodo_pago")
    private String paymentMethod;

    @Column(name = "monto")
    private double amount;

    @Column(name = "estado_pago")
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "orden_id")
    private Order order;
}
