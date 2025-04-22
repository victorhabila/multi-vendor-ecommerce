package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Deal;
import com.multi_vendo_ecom.ecommerce.multivendor.model.HomeCategory;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.DealRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.HomeCategoryRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.service.DealService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    public DealServiceImpl(DealRepository dealRepository, HomeCategoryRepository homeCategoryRepository) {
        this.dealRepository = dealRepository;
        this.homeCategoryRepository = homeCategoryRepository;
    }
    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        Deal newDeal = new Deal();
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws Exception {
        Deal existingDeal = dealRepository.findById(id).orElse(null);
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);

        if(existingDeal != null){
            if(deal.getDiscount()!= null){
                existingDeal.setDiscount(deal.getDiscount());
            }
            if(category!= null){
                existingDeal.setCategory(category);
            }
            return dealRepository.save(existingDeal);
        }
        throw new Exception("Deal not found");

    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        Deal deal = dealRepository.findById(id).orElseThrow(()-> new Exception("Deal not found"));
        dealRepository.delete(deal);
    }
}
