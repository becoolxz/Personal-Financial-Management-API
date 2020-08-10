package br.com.lucas.study.personalfinancialmanagementapi.service.impl;

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
    public UserDto findUserById(Long id) {

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
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
    public UserDto update(UserDto userDto) {

        User user = this.userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not Found"));

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());

        this.userRepository.save(user);

        UserDto userDtoResponse = new UserDto();

        userDtoResponse.setName(user.getName());
        userDtoResponse.setEmail(user.getEmail());

        return userDtoResponse;
    }

    @Override
    public User createNewUser(SignUpDto signUpDto) {
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setProfile(Role.ROLE_USER);
        user.setPassword(PasswordUtil.generateHashBCryptByPassword(signUpDto.getPassword()));

        this.userRepository.save(user);

        return user;
    }

}
