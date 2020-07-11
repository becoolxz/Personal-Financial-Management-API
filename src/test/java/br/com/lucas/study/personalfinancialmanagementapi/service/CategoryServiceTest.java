package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.repository.CategoryRepository;
import br.com.lucas.study.personalfinancialmanagementapi.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CategoryServiceTest {

    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        this.categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    @DisplayName("Should save a category")
    public void saveBookTest() {
        Category category = new Category();
        category.setDescription("Some description");

        Mockito.when(categoryRepository.save(category))
                .thenReturn(new Category(1L, "Some description"));

        Category savedCategory = categoryService.save(category);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getDescription()).isEqualTo("Some description");
    }
}
