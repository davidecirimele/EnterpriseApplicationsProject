package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.TransactionService;
import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/transactions", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(consumes =  "application/json", path = "/add")
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody TransactionDto transactionDto){
        TransactionDto addeddTransaction = transactionService.addTransactionDto(transactionDto);
        return new ResponseEntity<>(addeddTransaction, HttpStatus.CREATED);

    }

    @GetMapping(consumes =  "application/json", path = "/get/{email}")
    public ResponseEntity<List<TransactionDto>> getUserTransactions(@PathVariable String email){
        List<TransactionDto> transactions = transactionService.getAllTransactionByUserEmail(email);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
