package com.enterpriseapplicationsproject.ecommerce.controller;


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

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/paymentMethods", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor

public class PaymentMethodController {


    private final PaymentMethodsService paymentMethodService;

    @PostMapping(consumes = "application/json", path = "/add")
    @PreAuthorize("#paymentMethodDto.user.userId == authentication.principal.getId()")
    public ResponseEntity<PaymentMethodDto> addPaymentMethod( @Valid @RequestBody SavePaymentMethodDto paymentMethodDto) {
        System.out.println("PaymentMethodDto: " + paymentMethodDto.toString());

        PaymentMethodDto paymentMethod = paymentMethodService.addPaymentMethod(paymentMethodDto);
        return new ResponseEntity<>(paymentMethod, HttpStatus.CREATED);
    }

    @GetMapping(consumes = "application/json", path = "/get/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodByUserId(@PathVariable UUID userId) {
        PaymentMethodDto paymentMethod = paymentMethodService.getPaymentMethodByUserId(userId);
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }

    @DeleteMapping(consumes = "application/json", path = "/delete/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<PaymentMethodDto> deletePaymentMethodByUserId(@PathVariable UUID userId) {
        PaymentMethodDto paymentMethod = paymentMethodService.deletePaymentMethodByUserId(userId);
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }
}
