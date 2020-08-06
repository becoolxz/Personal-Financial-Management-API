package br.com.lucas.study.personalfinancialmanagementapi.resource.controller;

import br.com.lucas.study.personalfinancialmanagementapi.resource.dto.CategoryDTO;
import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.security.JwtAuthenticationEntryPoint;
import br.com.lucas.study.personalfinancialmanagementapi.security.utils.JwtTokenUtil;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    private static final String CATEGORY_API = "/api/categories";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("Should get information about a category by ID.")
    @WithMockUser(value = "Lucas")
    public void shouldGetCategoryByIdTest() throws Exception {

        long id = 1L;

        Category category = new Category(id, "Some category", Collections.emptyList());
        BDDMockito.given(categoryService.getById(id)).willReturn(Optional.of(category));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(CATEGORY_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value(1L))
                .andExpect(jsonPath("data.description").value("Some category"));

    }

    @Test
    @DisplayName("Should get the status 'ResourceNotFoundException' when the Category not exists with the " +
                 "ID informed for GET information about category.")
    @WithMockUser(value = "Lucas")
    public void shouldGetCategoryAndReturnNotFoundCategoryByIdTest() throws Exception {

        BDDMockito.given( categoryService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(CATEGORY_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create a new category.")
    @WithMockUser(value = "Lucas")
    public void shouldCreateCategoryTest() throws Exception {

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setDescription("Another description");

        Category savedCategory = new Category(1L,"Another description", Collections.emptyList());
        BDDMockito.given(categoryService.save(Mockito.any(Category.class))).willReturn(savedCategory);
        String json = new ObjectMapper().writeValueAsString(categoryDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CATEGORY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.id").value(1L))
                .andExpect(jsonPath("data.description").value(categoryDTO.getDescription() ));
    }

    @Test
    @DisplayName("Should throw an invalid category exception when it doesn't have enough data.")
    @WithMockUser(value = "Lucas")
    public void shouldCreateInvalidCategoryWithDescriptionEmptyTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new CategoryDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CATEGORY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(("errors"), hasSize(1)))
                .andExpect(jsonPath(("errors[0]")).value("The field 'description' must not be empty."));
    }

    @Test
    @DisplayName("Should update a category.")
    @WithMockUser(value = "Lucas")
    public void shouldUpdateCategoryByIdTest() throws Exception {

        long id = 1L;

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setDescription("Another Category");

        String json = new ObjectMapper().writeValueAsString(categoryDTO);

        Category updatingCategory = new Category(1L, "Some category", Collections.emptyList());

        BDDMockito.given(categoryService.getById(id)).willReturn(Optional.of(updatingCategory));

        BDDMockito.given(categoryService.update(updatingCategory))
                .willReturn(new Category(1L, "Another Category", Collections.emptyList()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(CATEGORY_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value(id))
                .andExpect(jsonPath("data.description").value("Another Category"));
    }

    @Test
    @DisplayName("Should get the exception 'ResourceNotFoundException' when the Category not exists with the " +
                 "ID informed for UPDATE category.")
    @WithMockUser(value = "Lucas")
    public void shouldUpdateCategoryAndReturnNotFoundCategoryById() throws Exception {

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

    @Test
    @DisplayName("Should delete a category.")
    @WithMockUser(value = "Lucas")
    public void shouldDeleteCategoryTest() throws Exception {

        BDDMockito.given(categoryService.getById(Mockito.anyLong()))
                .willReturn(Optional.of(new Category(1L,"Some category", Collections.emptyList())));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(CATEGORY_API.concat("/" + 1));

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should get the exception 'ResourceNotFoundException' when the Category not exists with the "+
                 "ID informed for DELETE category.")
    @WithMockUser(value = "Lucas")
    public void shouldDeleteCategoryAndReturnNotFoundIdCategoryByIdTest() throws Exception {

        BDDMockito.given(categoryService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(CATEGORY_API.concat("/" + 1));

        mvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

}
