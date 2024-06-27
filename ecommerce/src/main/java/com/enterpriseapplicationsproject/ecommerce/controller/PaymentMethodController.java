package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.data.service.PaymentMethodsService;
import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/paymentMethods", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor

public class PaymentMethodController {

    private final PaymentMethodsService paymentMethodService;

    @PostMapping(consumes = "application/json", path = "/add")
    public ResponseEntity<PaymentMethodDto> addPaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto) {
        PaymentMethodDto paymentMethod = paymentMethodService.addPaymentMethod(paymentMethodDto);
        return new ResponseEntity<>(paymentMethod, HttpStatus.CREATED);
    }

    @PostMapping(consumes = "application/json", path = "/get/{userId}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodByUserId(@PathVariable Long userId) {
        PaymentMethodDto paymentMethod = paymentMethodService.getPaymentMethodByUserId(userId);
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }

    @DeleteMapping(consumes = "application/json", path = "/delete/{userId}")
    public ResponseEntity<PaymentMethodDto> deletePaymentMethodByUserId(@PathVariable Long userId) {
        PaymentMethodDto paymentMethod = paymentMethodService.deletePaymentMethodByUserId(userId);
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }
}
