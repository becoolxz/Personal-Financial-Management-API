package br.com.lucas.study.personalfinancialmanagementapi.model.repository;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should save a transaction.")
    public void shouldSaveTransaction() {

        Transaction savingTransaction = createTransaction();

        entityManager.persist(savingTransaction.getCategory());

        Transaction savedTransaction = transactionRepository.save(savingTransaction);

        assertThat(savedTransaction).isNotNull();
    }

    public Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setCategory(new Category(null, "Some Category", Collections.emptyList()));
        transaction.setValue(400.0);
        transaction.setTypeTransaction(null);
        transaction.setYear("2020");
        transaction.setMonth(7);

        return transaction;
    }
}
