package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;

import java.util.Optional;

public interface CategoryService {

    Category save(Category category);

    Optional<Category> getById(long id);

    void delete(Category category);

    Category update(Category category);
}
