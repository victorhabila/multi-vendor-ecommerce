package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.model.HomeCategory;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.HomeCategoryRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.service.HomeCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;

    public HomeCategoryServiceImpl(HomeCategoryRepository homeCategoryRepository) {
        this.homeCategoryRepository = homeCategoryRepository;
    }

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategory(List<HomeCategory> homeCategories) {
        if(homeCategoryRepository.findAll().isEmpty()){
            return homeCategoryRepository.saveAll(homeCategories);
        }
        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory category, Long id) throws Exception {
        HomeCategory existingCategory = homeCategoryRepository.findById(id).orElseThrow( () -> new Exception("Category not found"));
        if(category.getImage() != null) {
            existingCategory.setImage(category.getImage());
        }
            if(category.getCategoryId() != null) {
                existingCategory.setCategoryId(category.getCategoryId());
            }
                return homeCategoryRepository.save(existingCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }
}
