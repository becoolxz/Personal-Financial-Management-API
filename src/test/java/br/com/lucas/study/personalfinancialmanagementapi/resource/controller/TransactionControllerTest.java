package br.com.lucas.study.personalfinancialmanagementapi.resource.controller;

import br.com.lucas.study.personalfinancialmanagementapi.model.enums.TypeTransaction;
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

    @Test
    @DisplayName("Should get transaction details by ID.")
    public void shouldGetTransactionDetailsByID() throws Exception {

        Long id = 1L;

        BDDMockito.given(transactionService.findById(id)).willReturn(Optional.of(createTransaction()));

        BDDMockito.given(categoryService.getById(createTransaction().getCategory().getId()))
                .willReturn(Optional.of(createTransaction().getCategory()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TRANSACTION_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value(1L))
                .andExpect(jsonPath("data.categoryName").value("Some Category"))
                .andExpect(jsonPath("data.description").value("Some Transaction"))
                .andExpect(jsonPath("data.value").value("400.0"))
                .andExpect(jsonPath("data.typeTransaction").value("SOME_TYPE"))
                .andExpect(jsonPath("data.year").value("2020"))
                .andExpect(jsonPath("data.month").value("7"));
    }


    @Test
    @DisplayName("Should return the exception 'ResourceNotFoundException' when the Transaction not exists with the " +
                 "ID informed for return information about transaction.")
    public void shouldGetNotfoundTransactionById() throws Exception {

        BDDMockito.given(transactionService.findById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TRANSACTION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return the exception 'ResourceNotFoundException' when the Category from Transaction not exists with the " +
                 "ID informed for return information about transaction.")
    public void shouldGetNotFoundCategoryTransactionById() throws Exception {

        Long id = 1L;

        BDDMockito.given(transactionService.findById(id)).willReturn(Optional.of(createTransaction()));

        BDDMockito.given(categoryService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TRANSACTION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update a transaction")
    public void shouldUpdateTransaction() throws Exception {
        long id = 1L;

        TransactionDTO transactionDTO = createTransactionDTO();

        String json = new ObjectMapper().writeValueAsString(transactionDTO);

        Transaction updatingTransaction = createTransaction();

        BDDMockito.given(transactionService.findById(id)).willReturn(Optional.of(updatingTransaction));

        Transaction updatedTransaction = createTransaction();

        updatedTransaction.setDescription("Another Description");
        updatedTransaction.setValue(600.0);

        BDDMockito.given(transactionService.update(updatingTransaction)).willReturn(updatedTransaction);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TRANSACTION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("description").value("Another Description"))
                .andExpect(jsonPath("value").value(600.0));


    }

    @Test
    @DisplayName("Should delete a transaction.")
    public void shouldDeleteTransactionTest() throws Exception {
        BDDMockito.given(transactionService.findById(Mockito.anyLong())).willReturn(Optional.of(createTransaction()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(TRANSACTION_API.concat("/" + 1));

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return the exception 'ResourceNotFoundException' when the Transaction not exists with the "+
                 "ID informed for delete transaction.")
    public void shouldDeleteNotFoundIdTransactionByIdTest() throws Exception {

        BDDMockito.given(transactionService.findById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(TRANSACTION_API.concat("/" + 1));

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    public TransactionDTO createTransactionDTO() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        transactionDTO.setCategoryId(1L);
        transactionDTO.setCategoryName("Some Category");
        transactionDTO.setDescription("Some Transaction");
        transactionDTO.setValue(400.0);
        transactionDTO.setTypeTransaction("SOME_TYPE");
        transactionDTO.setYear("2020");
        transactionDTO.setMonth("7");

        return transactionDTO;
    }

    public Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        transaction.setDescription("Some Transaction");

        transaction.setCategory(new Category(1L, "Some Category", Collections.emptyList()));

        transaction.setValue(400.0);
        transaction.setTypeTransaction(TypeTransaction.SOME_TYPE);
        transaction.setYear("2020");
        transaction.setMonth(7);

        return transaction;
    }

}
