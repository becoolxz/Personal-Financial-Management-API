package br.com.lucas.study.personalfinancialmanagementapi.controller;

import br.com.lucas.study.personalfinancialmanagementapi.dto.CategoryDTO;
import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    private static final String CATEGORY_API = "/api/categories";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("should create a new category.")
    public void shouldCreateCategoryTest() throws Exception {

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setDescription("Another description");

        Category savedCategory = new Category(1L,"Another description");
        BDDMockito.given(categoryService.save(Mockito.any(Category.class))).willReturn(savedCategory);
        String json = new ObjectMapper().writeValueAsString(categoryDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CATEGORY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("description").value(categoryDTO.getDescription() ));
    }

    @Test
    @DisplayName("should throw an invalid category exception when it doesn't have enough data.")
    public void shouldCreateInvalidCategoryTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new CategoryDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CATEGORY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(("errors"), hasSize(1)));
    }

    @Test
    @DisplayName("Should get information about a category by ID.")
    public void shouldGetCategoryDetailsByIdTest() throws Exception {

        long id = 1L;

        Category category = new Category(id, "Some category");
        BDDMockito.given(categoryService.getById(id)).willReturn(Optional.of(category));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(CATEGORY_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("description").value("Some category"));

    }

    @Test
    @DisplayName("Should return the exception 'ResourceNotFoundException' when the Category not exists with the " +
                 "ID informed for return information about category.")
    public void shouldGetNotFoundCategoryByIdTest() throws Exception {

        BDDMockito.given( categoryService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(CATEGORY_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete a category.")
    public void shouldDeleteCategoryTest() throws Exception {

        BDDMockito.given(categoryService.getById(Mockito.anyLong()))
                .willReturn(Optional.of(new Category(1L,"Some category")));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(CATEGORY_API.concat("/" + 1));

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Should return the exception 'ResourceNotFoundException' when the Category not exists with the "+
                 "ID informed for delete category.")
    public void shouldDeleteNotFoundIdCategoryByIdTest() throws Exception {

        BDDMockito.given(categoryService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(CATEGORY_API.concat("/" + 1));

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update a category.")
    public void shouldUpdateCategoryByIdTest() throws Exception {

        long id = 1L;

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setDescription("Another Category");

        String json = new ObjectMapper().writeValueAsString(categoryDTO);

        Category updatingCategory = new Category(1L, "Some category");

        BDDMockito.given(categoryService.getById(id)).willReturn(Optional.of(updatingCategory));

        BDDMockito.given(categoryService.update(updatingCategory)).willReturn(new Category(1L, "Another Category"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(CATEGORY_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("description").value("Another Category"));
    }

    @Test
    @DisplayName("Should return the exception 'ResourceNotFoundException' when the Category not exists with the " +
                 "ID informed for update category.")
    public void shouldUpdateNotFoundCategoryById() throws Exception {

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setDescription("Another Category");

        String json = new ObjectMapper().writeValueAsString(categoryDTO);

        BDDMockito.given(categoryService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(CATEGORY_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

}
