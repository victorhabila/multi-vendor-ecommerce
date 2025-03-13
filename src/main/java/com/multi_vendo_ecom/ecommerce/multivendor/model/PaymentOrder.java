package com.multi_vendo_ecom.ecommerce.multivendor.model;

import com.multi_vendo_ecom.ecommerce.multivendor.domain.PaymentMethod;
import com.multi_vendo_ecom.ecommerce.multivendor.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // because of the SET collection
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    private PaymentMethod paymentMethod;

    private String paymentLinkId;


    @ManyToOne
    private User user;

    @OneToMany //a user can decide to buy paint and bags from separate sellers or brands. This will require two or more orders created for paint and bags
    private Set<Order> orders = new HashSet<>();

}
