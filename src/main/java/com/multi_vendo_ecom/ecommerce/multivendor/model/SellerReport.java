package com.multi_vendo_ecom.ecommerce.multivendor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Seller seller;

    private Long totalEarnings = 0L;

    Long totalSales= 0L;

    private Long totalRefunds= 0L;

    private Long totalTax = 0L;

    private Long netEarnings = 0L;

    private Integer totalOrders= 0;

    private Integer canceledOrders=0;

    private Integer totalTransactions=0;


}
