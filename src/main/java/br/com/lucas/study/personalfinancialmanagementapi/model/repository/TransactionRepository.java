package br.com.lucas.study.personalfinancialmanagementapi.model.repository;

import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
