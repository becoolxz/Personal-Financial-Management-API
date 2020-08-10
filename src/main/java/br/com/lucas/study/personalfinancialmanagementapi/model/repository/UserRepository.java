package br.com.lucas.study.personalfinancialmanagementapi.model.repository;

import br.com.lucas.study.personalfinancialmanagementapi.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
