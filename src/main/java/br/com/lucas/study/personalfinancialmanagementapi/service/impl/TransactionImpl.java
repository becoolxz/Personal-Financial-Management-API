package br.com.lucas.study.personalfinancialmanagementapi.service.impl;

import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import br.com.lucas.study.personalfinancialmanagementapi.model.repository.TransactionRepository;
import br.com.lucas.study.personalfinancialmanagementapi.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        if (verifyTransactionAndTransactionIdIsNull(transaction)) {
            throw new IllegalArgumentException("Transaction ID can't be null.");
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public void delete(Transaction transaction) {
        if (verifyTransactionAndTransactionIdIsNull(transaction)) {
            throw new IllegalArgumentException("Transaction ID can't be null.");
        }

        transactionRepository.delete(transaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    private boolean verifyTransactionAndTransactionIdIsNull(Transaction transaction) {
        return transaction == null || transaction.getId() == null;
    }

}
