package br.com.lucas.study.personalfinancialmanagementapi.service.impl;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.model.repository.CategoryRepository;
import br.com.lucas.study.personalfinancialmanagementapi.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getById(long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public void delete(Category category) {
        if (verifyCategoryAndCategoryIdIsNull(category)) {
            throw new IllegalArgumentException("Category ID can't be null.");
        }
        this.categoryRepository.delete(category);
    }

    @Override
    public Category update(Category category) {
        if(verifyCategoryAndCategoryIdIsNull(category)) {
            throw new IllegalArgumentException("Category ID can't be null.");
        }
        return this.categoryRepository.save(category);
    }

    private boolean verifyCategoryAndCategoryIdIsNull(Category category) {
        return category == null || category.getId() == null;
    }

}
