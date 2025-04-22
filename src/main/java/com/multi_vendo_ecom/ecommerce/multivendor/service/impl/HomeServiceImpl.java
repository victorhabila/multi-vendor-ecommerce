package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.domain.HomeCategorySection;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Deal;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Home;
import com.multi_vendo_ecom.ecommerce.multivendor.model.HomeCategory;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.DealRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.service.HomeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

    private final DealRepository dealRepository;

    public HomeServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories = allCategories.stream().filter(category -> category.getSection() == HomeCategorySection.GRID)
                .collect(Collectors.toList());

        List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES)
                .collect(Collectors.toList());

        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES)
                .collect(Collectors.toList());

        List<HomeCategory> dealCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                .toList();
        List<Deal> createdDeals = new ArrayList<>();

        if(dealRepository.findAll().isEmpty()){
            List<Deal> deals = allCategories.stream()
                    .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .collect(Collectors.toList());
            createdDeals = dealRepository.saveAll(deals);
        } else createdDeals = dealRepository.findAll();

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setElectricCategories(electricCategories);
        home.setDeals(createdDeals);
        home.setDealCategories(dealCategories);

        return home;
    }
}
