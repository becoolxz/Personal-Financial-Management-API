package br.com.lucas.study.personalfinancialmanagementapi.resource.controller;

import br.com.lucas.study.personalfinancialmanagementapi.resource.dto.TransactionDTO;
import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.Transaction;
import br.com.lucas.study.personalfinancialmanagementapi.service.CategoryService;
import br.com.lucas.study.personalfinancialmanagementapi.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {

    private static final String TRANSACTION_API = "/api/transactions";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private TransactionService transactionService;

    @Test
    @DisplayName("Should create a transaction")
    public void shouldCreateTransactionTest() throws Exception {

        TransactionDTO transactionDTO = createTransactionDTO();
        String json = new ObjectMapper().writeValueAsString(transactionDTO);

        BDDMockito.given(categoryService.getById(1L))
                .willReturn(Optional.of(new Category(1L, "Some Category", Collections.emptyList())));

        Transaction transaction = createTransaction();

        BDDMockito.given(transactionService.save(Mockito.any(Transaction.class))).willReturn(transaction);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TRANSACTION_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data").value("Transaction saved successfully!!!"));
    }

    @Test
    @DisplayName("Should return the exception 'ResourceNotFoundException' when the Category not exists with the " +
            "ID informed for return information about Transaction.")
    public void shouldGetNotFoundCategoryByIdWhenCreateATransaction() throws Exception {

        TransactionDTO transactionDTO = createTransactionDTO();
        String json = new ObjectMapper().writeValueAsString(transactionDTO);

        BDDMockito.given(categoryService.getById(1L)).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TRANSACTION_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Category not found for create a new transaction"));

    }

    public TransactionDTO createTransactionDTO() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        transactionDTO.setCategoryId(1L);
        transactionDTO.setCategoryName("Some Category");
        transactionDTO.setCategoryName("Some Transaction");
        transactionDTO.setValue("400");
        transactionDTO.setTypeTransaction("SOME_TYPE");
        transactionDTO.setYear("2020");
        transactionDTO.setMonth("7");

        return transactionDTO;
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
