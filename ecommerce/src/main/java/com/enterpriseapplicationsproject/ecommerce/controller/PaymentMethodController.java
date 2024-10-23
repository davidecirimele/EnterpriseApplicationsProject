package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.service.PaymentMethodsService;
import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SavePaymentMethodDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/paymentMethods", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor

public class PaymentMethodController {


    private final PaymentMethodsService paymentMethodService;

    @RateLimit(type ="USER")
    @PostMapping(consumes = "application/json", path = "/add")
    @PreAuthorize("#paymentMethodDto.user.userId == authentication.principal.getId()")
    public ResponseEntity<PaymentMethodDto> addPaymentMethod( @Valid @RequestBody SavePaymentMethodDto paymentMethodDto) {
        System.out.println("PaymentMethodDto: " + paymentMethodDto.toString());

        PaymentMethodDto paymentMethod = paymentMethodService.addPaymentMethod(paymentMethodDto);
        return new ResponseEntity<>(paymentMethod, HttpStatus.CREATED);
    }

    @RateLimit(type ="USER")
    @GetMapping(path = "/get/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethodByUserId(@PathVariable UUID userId) {
        List<PaymentMethodDto> paymentMethod = paymentMethodService.getAllPaymentMethodByUserId(userId);
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }

    @RateLimit(type ="USER")
    @GetMapping(path = "/get/{userId}/{paymentMethodId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodByUserId(@PathVariable UUID userId, @PathVariable Long paymentMethodId) {
        PaymentMethodDto paymentMethod = paymentMethodService.getPaymentMethodByUserId(userId, paymentMethodId);
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }

    @RateLimit(type ="USER")
    @DeleteMapping(path = "/delete/{paymentMethodId}/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<Void> deletePaymentMethodByUserId(@PathVariable UUID userId, @PathVariable Long paymentMethodId) {
       paymentMethodService.deletePaymentMethodByUserId(userId, paymentMethodId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
