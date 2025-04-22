package com.multi_vendo_ecom.ecommerce.multivendor.service;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Home;
import com.multi_vendo_ecom.ecommerce.multivendor.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
