package br.com.lucas.study.personalfinancialmanagementapi.service.impl;

import br.com.lucas.study.personalfinancialmanagementapi.endpoint.exception.UserAlreadyExistsException;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.exception.UserNotFoundException;
import br.com.lucas.study.personalfinancialmanagementapi.model.User;
import br.com.lucas.study.personalfinancialmanagementapi.model.enums.Role;
import br.com.lucas.study.personalfinancialmanagementapi.model.repository.UserRepository;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.SignUpDto;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.UserDto;
import br.com.lucas.study.personalfinancialmanagementapi.service.UserService;
import br.com.lucas.study.personalfinancialmanagementapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByUsernameEmail(String usernameEmail) {
        return this.userRepository.findByEmail(usernameEmail)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));
    }

    @Override
    public User findUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(UserDto userDto) {

        User user = this.userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not Found"));

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());

        return this.userRepository.save(user);

    }

    @Override
    public User createNewUser(SignUpDto signUpDto) {

        verifyUserAlreadyExistsWithUsernameEmail(signUpDto.getEmail());

        User newUser = new User();

        newUser.setName(signUpDto.getName());
        newUser.setEmail(signUpDto.getEmail());
        newUser.setProfile(Role.ROLE_USER);
        newUser.setPassword(PasswordUtil.generateHashBCryptByPassword(signUpDto.getPassword()));

        return this.userRepository.save(newUser);
    }

    @Override
    public void verifyUserAlreadyExistsWithUsernameEmail(String usernameEmail) {
        Optional<User> userOpt = this.userRepository.findByEmail(usernameEmail);
        if (userOpt.isPresent()) {
            throw new UserAlreadyExistsException("User already exits with email: " + usernameEmail);
        }
    }

}
