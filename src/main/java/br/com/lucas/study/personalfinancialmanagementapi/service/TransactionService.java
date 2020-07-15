package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TransactionService {

    Transaction save(Transaction transaction);

    Transaction update(Transaction transaction);

    void delete(Transaction transaction);

    Optional<Transaction> getById(Long id);
}
