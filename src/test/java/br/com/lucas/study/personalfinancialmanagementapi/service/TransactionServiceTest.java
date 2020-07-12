package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import br.com.lucas.study.personalfinancialmanagementapi.model.repository.TransactionRepository;
import br.com.lucas.study.personalfinancialmanagementapi.service.impl.TransactionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        this.transactionService = new TransactionImpl(transactionRepository);
    }

    @Test
    @DisplayName("Should save a transaction.")
    public void shouldSaveTransaction() {
        Transaction savingTransaction = createTransaction();
        savingTransaction.setId(null);

        Transaction savedTransaction = createTransaction();

        when(transactionRepository.save(savingTransaction)).thenReturn(savedTransaction);

        Transaction transaction = transactionService.save(savingTransaction);

        assertThat(transaction.getId()).isEqualTo(savedTransaction.getId());
        assertThat(transaction.getCategory().getId()).isEqualTo(savedTransaction.getCategory().getId());
        assertThat(transaction.getDescription()).isEqualTo(savedTransaction.getDescription());
        assertThat(transaction.getTypeTransaction()).isEqualTo(savedTransaction.getTypeTransaction());
        assertThat(transaction.getValue()).isEqualTo(savedTransaction.getValue());
        assertThat(transaction.getMonth()).isEqualTo(savedTransaction.getMonth());
        assertThat(transaction.getYear()).isEqualTo(savedTransaction.getYear());
    }

    public Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCategory(new Category(1L, "Some Category", Collections.emptyList()));
        transaction.setValue(400.0);
        transaction.setTypeTransaction(null);
        transaction.setYear("2020");
        transaction.setMonth(7);

        return transaction;
    }
}
