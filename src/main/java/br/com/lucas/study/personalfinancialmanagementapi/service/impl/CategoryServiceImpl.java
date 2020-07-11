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
        return Optional.empty();
    }

    @Override
    public void delete(Category category) {

    }

    @Override
    public Category update(Category category) {
        return null;
    }

}
