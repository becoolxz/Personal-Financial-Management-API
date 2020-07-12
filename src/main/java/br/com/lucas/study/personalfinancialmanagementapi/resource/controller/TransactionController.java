package br.com.lucas.study.personalfinancialmanagementapi.resource.controller;

import br.com.lucas.study.personalfinancialmanagementapi.resource.dto.TransactionDTO;
import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import br.com.lucas.study.personalfinancialmanagementapi.model.enums.TypeTransaction;
import br.com.lucas.study.personalfinancialmanagementapi.resource.response.Response;
import br.com.lucas.study.personalfinancialmanagementapi.service.CategoryService;
import br.com.lucas.study.personalfinancialmanagementapi.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    private final CategoryService categoryService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Response<String>> create(@RequestBody TransactionDTO transactionDTO) {

        Response<String> response = new Response<>();

        Category category =  categoryService.getById(transactionDTO.getCategoryId())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found for create a new transaction"));

        Transaction transaction = new Transaction();
        transaction.setCategory(category);
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setTypeTransaction(TypeTransaction.valueOf(transactionDTO.getTypeTransaction()));
        transaction.setValue(Double.valueOf(transactionDTO.getValue()));
        transaction.setMonth(Integer.valueOf(transactionDTO.getMonth()));
        transaction.setYear(transactionDTO.getYear());

        transactionService.save(transaction);

        response.setData("Transaction saved successfully!!!");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
