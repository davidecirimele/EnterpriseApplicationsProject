package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.ProductsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Product;
import com.enterpriseapplicationsproject.ecommerce.data.service.ProductsService;
import com.enterpriseapplicationsproject.ecommerce.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    private final ProductsDao productsDao;

    private final ModelMapper modelMapper;

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        Product p = productsDao.save(product);
        return modelMapper.map(p, ProductDto.class);
    }

//    @Override
//    public List<ProductDto> findAll(Specification<Product> spec) {
//        List<Product> products = productsDao.findAll(spec);
//        return products.stream()
//                .map(p -> modelMapper.map(p, ProductDto.class))
//                .collect(Collectors.toList());
//    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productsDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Product not found with id [%s]", id)));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {
        Product product = productsDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Product not found with id [%s]", id)));
        modelMapper.map(productDto, product);
        Product updatedProduct = productsDao.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void delete(Long id) {
        productsDao.deleteById(id);
    }

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productsDao.findAll();
        return products.stream().map(p -> modelMapper.map(p, ProductDto.class)).collect(Collectors.toList());
    }

}
