package com.multi_vendo_ecom.ecommerce.multivendor.service;

import com.multi_vendo_ecom.ecommerce.multivendor.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {

    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createCategory(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception;

    List<HomeCategory> getAllHomeCategories();
}
