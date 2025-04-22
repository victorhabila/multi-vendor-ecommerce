package com.multi_vendo_ecom.ecommerce.multivendor.repository;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
