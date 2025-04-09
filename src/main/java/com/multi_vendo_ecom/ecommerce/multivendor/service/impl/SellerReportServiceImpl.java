package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import com.multi_vendo_ecom.ecommerce.multivendor.model.SellerReport;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.SellerReportRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.SellerRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.service.SellerReportService;
import org.springframework.stereotype.Service;

@Service
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;

    public SellerReportServiceImpl(SellerReportRepository sellerReportRepository) {
        this.sellerReportRepository = sellerReportRepository;

    }

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sr = sellerReportRepository.findBySellerId(seller.getId());
        if(sr==null){
            SellerReport newReport = new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepository.save(newReport);
        }
        return sr;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
