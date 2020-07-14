package br.com.lucas.study.personalfinancialmanagementapi.resource.controller;

import br.com.lucas.study.personalfinancialmanagementapi.resource.dto.TransactionDTO;
import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import br.com.lucas.study.personalfinancialmanagementapi.model.enums.TypeTransaction;
import br.com.lucas.study.personalfinancialmanagementapi.resource.response.Response;
import br.com.lucas.study.personalfinancialmanagementapi.service.CategoryService;
import br.com.lucas.study.personalfinancialmanagementapi.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    private final CategoryService categoryService;

    private final ModelMapper modelMapper;

    public TransactionController(TransactionService transactionService, CategoryService categoryService,
                                 ModelMapper modelMapper) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
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
        transaction.setValue(transactionDTO.getValue());
        transaction.setMonth(Integer.valueOf(transactionDTO.getMonth()));
        transaction.setYear(transactionDTO.getYear());

        transactionService.save(transaction);

        response.setData("Transaction saved successfully!!!");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<TransactionDTO>> get(@PathVariable Long id) {
        Response<TransactionDTO> response = new Response<>();

        Transaction transaction = transactionService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Category category = categoryService.getById(transaction.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        transactionDTO.setCategoryId(category.getId());
        transactionDTO.setCategoryName(category.getDescription());

        response.setData(transactionDTO);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("{id}")
    public TransactionDTO update(@PathVariable Long id, TransactionDTO transactionDTO) {
        return transactionService.findById(id).map(transaction -> {

            transaction.setDescription(transactionDTO.getDescription());
            transaction.setValue(transactionDTO.getValue());
            transaction = transactionService.update(transaction);

            return modelMapper.map(transaction, TransactionDTO.class);
        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Transaction transaction = transactionService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        transactionService.delete(transaction);
    }
}
