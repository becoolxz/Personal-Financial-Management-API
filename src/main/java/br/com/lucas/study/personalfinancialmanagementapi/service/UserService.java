package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.model.User;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.SignUpDto;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.UserDto;

public interface UserService {

    User findUserByUsernameEmail(String usernameEmail);

    UserDto findUserById(Long id);

    User save(User user);

    void delete(Long id);

    UserDto update(UserDto userDto);

    User createNewUser(SignUpDto signUpDto);

}
