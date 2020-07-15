package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;

import java.util.Optional;

public interface CategoryService {

    Optional<Category> getById(long id);

    Category save(Category category);

    Category update(Category category);

    void delete(Category category);

}
