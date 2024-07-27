package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.ProductsService;
import com.enterpriseapplicationsproject.ecommerce.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsService productsService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getById(@PathVariable("productId") Long id) {
        ProductDto p = productsService.findById(id);
        if (p==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable("productId") Long id, @RequestBody ProductDto productDto) {
        ProductDto p = productsService.update(id, productDto);
        return ResponseEntity.ok(p);
    }

    @PostMapping()
    public ResponseEntity<ProductDto> add(@RequestBody ProductDto productDto){
        ProductDto p = productsService.save(productDto);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{productId}")
    public HttpStatus delete(@PathVariable("productId") Long id) {
        productsService.delete(id);
        return HttpStatus.OK;
    }
}
