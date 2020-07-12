package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    Transaction save(Transaction transaction);

}
