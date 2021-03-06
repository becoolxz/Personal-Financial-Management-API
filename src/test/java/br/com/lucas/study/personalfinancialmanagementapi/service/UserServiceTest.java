package br.com.lucas.study.personalfinancialmanagementapi.service;

import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.UserDto;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.exception.UserAlreadyExistsException;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.exception.UserNotFoundException;
import br.com.lucas.study.personalfinancialmanagementapi.model.User;
import br.com.lucas.study.personalfinancialmanagementapi.model.enums.Role;
import br.com.lucas.study.personalfinancialmanagementapi.model.repository.UserRepository;
import br.com.lucas.study.personalfinancialmanagementapi.service.impl.UserServiceImpl;
import br.com.lucas.study.personalfinancialmanagementapi.util.PasswordUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        this.userService = new UserServiceImpl(userRepository);
    }


    @Test
    @DisplayName("Should get information about a user by EMAIL.")
    public void shouldGetUserByEmail() {

        User user = createUser();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByUsernameEmail(user.getEmail());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getName()).isEqualTo(user.getName());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(foundUser.getProfile()).isEqualTo(user.getProfile());
    }

    @Test
    @DisplayName("Should throw exception when not found user by email.")
    public void shouldGetUserAndNotFoundUserByEmail() {

        String email = "lucas@gmail.com";

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserByUsernameEmail(email));

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should get information about a user by id.")
    public void shouldGetUserById() {

        User user = createUser();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = userService.findUserById(user.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getName()).isEqualTo(user.getName());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when not found user by id.")
    public void shouldGetUserAndNotFoundUserById() {

        Long id = 1L;

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserById(id));

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("Should save a user.")
    public void shouldSaveUser() {

        User newUser = createUser();

        newUser.setId(null);

        User savedUser = createUser();

        Mockito.when(userRepository.save(newUser)).thenReturn(savedUser);

        userService.save(newUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(savedUser.getName()).isEqualTo(newUser.getName());
        assertThat(savedUser.getProfile()).isEqualTo(newUser.getProfile());
    }

    @Test
    @DisplayName("Should not throw exception when user already exists with username email when saving a user.")
    public void shouldVerifyUsernameEmailAlreadyExistsAndNotReturnUserAlreadyExistsException() {

        String usernameEmail = "lucas@email.com";

        BDDMockito.given(userRepository.findByEmail(usernameEmail)).willReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> userService.verifyUserAlreadyExistsWithUsernameEmail(usernameEmail));

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(usernameEmail);
    }


    @Test
    @DisplayName("Should throw exception when user already exists with username email when saving a user.")
    public void shouldVerifyUsernameEmailAlreadyExistsAndReturnUserAlreadyExistsException() {

        String usernameEmail = "lucas@email.com";

        User foundUser = createUser();

        BDDMockito.given(userRepository.findByEmail(usernameEmail)).willReturn(Optional.of(foundUser));

        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.verifyUserAlreadyExistsWithUsernameEmail(usernameEmail));

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(usernameEmail);
    }

    @Test
    @DisplayName("Should update a user.")
    public void shouldUpdateUser() {

        UserDto updatingUserDto = createUserDto();

        updatingUserDto.setName("Marcos");

        User foundUser = createUser();

        User updatedMockUser = createUser();
        updatedMockUser.setName("Marcos");

        Mockito.when(userRepository.findById(updatingUserDto.getId())).thenReturn(Optional.of(foundUser));

        Mockito.when(userRepository.save(foundUser)).thenReturn(updatedMockUser);

        User updatedUser = userService.update(updatingUserDto);

        assertThat(updatedUser.getId()).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo(updatingUserDto.getEmail());
        assertThat(updatedUser.getName()).isEqualTo(updatingUserDto.getName());
    }

    @Test
    @DisplayName("Should delete a user.")
    public void shouldDeleteUser() {

        User user = createUser();

        Assertions.assertDoesNotThrow( () -> userService.delete(user.getId()));

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Lucas");
        user.setEmail("lucas@email.com");
        user.setPassword(PasswordUtil.generateHashBCryptByPassword("123123"));
        user.setProfile(Role.ROLE_USER);

        return user;
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("Lucas");
        userDto.setEmail("lucas@email.com");
        userDto.setPassword("123123");

        return userDto;
    }
}
