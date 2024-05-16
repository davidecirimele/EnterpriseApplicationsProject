package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.TransactionService;
import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/transactions", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(consumes =  "application/json")
    public void addTransaction(@RequestBody TransactionDto transactionDto){
        transactionService.addTransactionDto(transactionDto);

    }

}
