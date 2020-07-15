package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import br.com.lucas.study.personalfinancialmanagementapi.model.enums.TypeTransaction;
import br.com.lucas.study.personalfinancialmanagementapi.model.repository.TransactionRepository;
import br.com.lucas.study.personalfinancialmanagementapi.service.impl.TransactionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

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
    @DisplayName("Should find a transaction by ID.")
    public void shouldGetTransactionById() {

        Transaction transaction = createTransaction();

        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        Optional<Transaction> foundTransaction = transactionService.getById(transaction.getId());

        assertThat(foundTransaction.isPresent()).isTrue();
        assertThat(foundTransaction.get().getId()).isEqualTo(1L);
        assertThat(foundTransaction.get().getDescription()).isEqualTo("Some transaction");
        assertThat(foundTransaction.get().getCategory()).isNotNull();
        assertThat(foundTransaction.get().getValue()).isEqualTo(400.0);
        assertThat(foundTransaction.get().getTypeTransaction()).isEqualTo(TypeTransaction.OUTPUT);
        assertThat(foundTransaction.get().getMonth()).isEqualTo(7);
        assertThat(foundTransaction.get().getYear()).isEqualTo("2020");
    }

    @Test
    @DisplayName("Should get empty transaction object by ID.")
    public void shouldGetTransactionAndNotFoundTransactionById() {

        long id = 1L;

        Mockito.when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Transaction> transactionOpt = transactionService.getById(id);

        assertThat(transactionOpt.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should save a new transaction.")
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

    @Test
    @DisplayName("Should update a transaction.")
    public void shouldUpdateTransaction() {

        Transaction updatingTransaction = createTransaction();

        when(transactionRepository.save(updatingTransaction)).thenReturn(updatingTransaction);

        updatingTransaction = transactionService.update(updatingTransaction);

        assertThat(updatingTransaction).isNotNull();
        assertThat(updatingTransaction.getId()).isEqualTo(updatingTransaction.getId());
        assertThat(updatingTransaction.getDescription()).isEqualTo(updatingTransaction.getDescription());
        assertThat(updatingTransaction.getValue()).isEqualTo(updatingTransaction.getValue());
        assertThat(updatingTransaction.getTypeTransaction()).isEqualTo(updatingTransaction.getTypeTransaction());
        assertThat(updatingTransaction.getMonth()).isEqualTo(updatingTransaction.getMonth());
        assertThat(updatingTransaction.getYear()).isEqualTo(updatingTransaction.getYear());

    }

    @Test
    @DisplayName("Should throw an exception when trying to update a non-existent transaction.")
    public void shouldUpdateInvalidTransactionId() {

        Transaction transaction = new Transaction();

        Assertions.assertThrows(IllegalArgumentException.class, () -> transactionService.update(transaction));

        Mockito.verify(transactionRepository, Mockito.never()).save(transaction);

    }

    @Test
    @DisplayName("Should delete a transaction.")
    public void shouldDeleteTransaction() {

        Transaction transaction = createTransaction();

        Assertions.assertDoesNotThrow( ()-> transactionService.delete(transaction));

        Mockito.verify(transactionRepository, Mockito.times(1)).delete(transaction);

    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a non-existent transaction.")
    public void shouldDeleteInvalidTransaction()  {

        Transaction transaction = new Transaction();

        Assertions.assertThrows(IllegalArgumentException.class,  ()-> transactionService.delete(transaction));

        Mockito.verify(transactionRepository, Mockito.never()).delete(transaction);

    }

    public Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Some transaction");
        transaction.setCategory(new Category(1L, "Some Category", Collections.emptyList()));
        transaction.setValue(400.0);
        transaction.setTypeTransaction(TypeTransaction.OUTPUT);
        transaction.setYear("2020");
        transaction.setMonth(7);

        return transaction;
    }
}
