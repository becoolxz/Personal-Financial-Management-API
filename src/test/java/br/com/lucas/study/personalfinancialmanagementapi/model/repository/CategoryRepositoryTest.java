package br.com.lucas.study.personalfinancialmanagementapi.model.repository;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should save a category.")
    public void shouldSaveCategory() {
        Category category = new Category(null, "Some Category", Collections.emptyList());

        Category savedCategory =  categoryRepository.save(category);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getDescription()).isEqualTo("Some Category");
    }

    @Test
    @DisplayName("Should get a book by ID.")
    public void shouldFindCategoryById() {
        Category category = new Category(null, "Some description", Collections.emptyList());

        category = entityManager.persist(category);

        Optional<Category> foundCategory = categoryRepository.findById(category.getId());

        assertThat(foundCategory.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should delete a Category.")
    public void shouldDeleteCategoryTest() {
        Category category = new Category(null, "Some description", Collections.emptyList());

        entityManager.persist(category);

        Category foundCategory = entityManager.find(Category.class, category.getId());

        categoryRepository.delete(foundCategory);

        Category deletedCategory = entityManager.find(Category.class, category.getId());

        assertThat(deletedCategory).isNull();
    }

}
