package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Product;
import com.enterpriseapplicationsproject.ecommerce.dto.ProductDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductsService {
    ProductDto save(ProductDto productDto);

//    List<ProductDto> findAll(Specification<Product> spec);

    ProductDto findById(Long id);

    ProductDto update(Long id, ProductDto productDto);

    void delete(Long id);

    void updateProductStock(Long id, int quantity);
}
