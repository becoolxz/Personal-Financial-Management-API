package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.repository.CategoryRepository;
import br.com.lucas.study.personalfinancialmanagementapi.service.impl.CategoryServiceImpl;
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
    @DisplayName("Should get information about a category by ID.")
    public void shouldGetCategoryByIdTest() {
        Long id = 1L;

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(new Category(id, "Some description", Collections.emptyList())));

        Optional<Category> foundCategory = categoryService.getById(id);

        assertThat(foundCategory.isPresent()).isTrue();
        assertThat(foundCategory.get().getId()).isEqualTo(id);
        assertThat(foundCategory.get().getDescription()).isEqualTo("Some description");
    }

    @Test
    @DisplayName("Should get empty category object by ID.")
    public void shouldGetCategoryAndNotFoundCategoryById() {

        long id = 1L;

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Category> category = categoryService.getById(id);

        assertThat(category.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should save a new category.")
    public void shouldSaveNewCategoryTest() {

        Category category = new Category();
        category.setDescription("Some description");

        Mockito.when(categoryRepository.save(category))
                .thenReturn(new Category(1L, "Some description", Collections.emptyList()));

        Category savedCategory = categoryService.save(category);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getDescription()).isEqualTo("Some description");
    }

    @Test
    @DisplayName("Should update a category by ID.")
    public void shouldUpdateCategoryTestById() {

        Category updatingCategory = new Category(1L, "Some description", Collections.emptyList());

        Mockito.when(categoryRepository.save(updatingCategory)).thenReturn(updatingCategory);

        updatingCategory = categoryService.update(updatingCategory);

        assertThat(updatingCategory.getId()).isEqualTo(1L);
        assertThat(updatingCategory.getDescription()).isEqualTo("Some description");
    }

    @Test
    @DisplayName("Should throw an exception when trying to update a non-existent category.")
    public void shouldUpdateInvalidCategoryById() {

        Category category = new Category();

        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.update(category));

        Mockito.verify(categoryRepository, Mockito.never()).save(category);
    }

    @Test
    @DisplayName("Should delete a category by ID.")
    public void shouldDeleteCategoryTestById() {

        Category category = new Category(1L, "Some description", Collections.emptyList());

        Assertions.assertDoesNotThrow( () -> categoryService.delete(category));

        Mockito.verify(categoryRepository, Mockito.times(1)).delete(category);
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a non-existent category.")
    public void shouldDeleteInvalidCategoryTestById() {

        Category category = new Category();

        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.delete(category));

        Mockito.verify( categoryRepository, Mockito.never()).delete(category);
    }

}
