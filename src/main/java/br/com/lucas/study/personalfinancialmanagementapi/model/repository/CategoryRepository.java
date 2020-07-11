package br.com.lucas.study.personalfinancialmanagementapi.model.repository;

import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
