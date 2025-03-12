package com.multi_vendo_ecom.ecommerce.multivendor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.multi_vendo_ecom.ecommerce.multivendor.domain.USER_ROLE;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // this will restrict fetching or modifying password
    private String password;

    private String email;

    private String fullName;

    private String mobile;

    private USER_ROLE role = USER_ROLE.USER_CUSTOMER;

    @OneToMany()
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany
    @JsonIgnore //this is because we don't need it in frontend only in backend to know that this coupon is used or not
    private Set<Coupon> usedCoupons = new HashSet<>();

}
