package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.exceptions.ProductException;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Category;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Product;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.CategoryRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.ProductRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.request.CreateProductRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(req.getCategory());
        if (category1 == null) {
            category1 = new Category();
            category1.setCategoryId(req.getCategory());
            category1.setLevel(1);
            category1 = categoryRepository.save(category1);
        }

        // Handle category2
        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
        if (category2 == null && req.getCategory2() != null) { // Check if category2 is provided
            category2 = new Category();
            category2.setCategoryId(req.getCategory2()); // Use req.getCategory2()
            category2.setLevel(2);
            category2.setParentCategory(category1);
            category2 = categoryRepository.save(category2);
        }

        // Handle category3
        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());
        if (category3 == null && req.getCategory3() != null) { // Check if category3 is provided
            category3 = new Category();
            category3.setCategoryId(req.getCategory3()); // Use req.getCategory3()
            category3.setLevel(3);
            category3.setParentCategory(category2);
            category3 = categoryRepository.save(category3);
        }

        // Calculate discount percentage
        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        // Create and save the product
        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3); // Assuming the product belongs to the deepest category (category3)
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountPercent(discountPercentage);

        return productRepository.save(product);

    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {

        if(mrpPrice <= 0){
            throw new IllegalArgumentException("Actual price must be greater than 0");
        }
        double discount=mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice)*100;
        return (int)discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product1 = findProductById(productId);
        productRepository.delete(product1);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {

        findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(()-> new ProductException("product not found with id "+ productId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder)-> {
            List<Predicate> predicates = new ArrayList<>();//import predicate from jarkarta persistence criteria

            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (colors != null && !colors.isEmpty()) {

                System.out.println("color " + colors);
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
            // Filter by size (single value)
            if (sizes != null && !sizes.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("size"), sizes));

            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"),
                        minPrice));

            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"),
                        maxPrice));
            }

            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"),
                        minDiscount));
            }
            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

            Pageable pageable;
            if(sort!=null && !sort.isEmpty()){
                pageable = switch (sort) {
                    case "price_low" ->
                            PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                    case "price_high" ->
                            PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                    default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
                };
            }else{
                pageable = PageRequest.of(pageNumber != null ? pageNumber:0, 10, Sort.unsorted());
            }
      return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
