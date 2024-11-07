package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.service.TransactionsService;
import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/transactions", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionsService transactionsService;


    @RateLimit(type ="USER")
    @GetMapping(path = "/get/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<List<TransactionDto>> getUserTransactions(@PathVariable UUID userId){
        List<TransactionDto> transactions = transactionsService.getAllTransactionByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
