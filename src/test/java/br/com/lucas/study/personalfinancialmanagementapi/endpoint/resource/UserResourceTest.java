package br.com.lucas.study.personalfinancialmanagementapi.endpoint.resource;

import br.com.lucas.study.personalfinancialmanagementapi.model.User;
import br.com.lucas.study.personalfinancialmanagementapi.model.enums.Role;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.SignUpDto;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.UserDto;
import br.com.lucas.study.personalfinancialmanagementapi.security.JwtAuthenticationEntryPoint;
import br.com.lucas.study.personalfinancialmanagementapi.security.JwtUser;
import br.com.lucas.study.personalfinancialmanagementapi.security.utils.JwtTokenUtil;
import br.com.lucas.study.personalfinancialmanagementapi.service.impl.UserServiceImpl;
import br.com.lucas.study.personalfinancialmanagementapi.util.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserResource.class)
@AutoConfigureMockMvc
public class UserResourceTest {

    private static final String USER_API = "/api/users";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Test
    @DisplayName("Should return a User by JwtUser.")
    public void shouldReturnUserByJwtUserTest() throws Exception {

        UserDto userDto = createUserDto();

        JwtUser jwtUser = createJwtUser();

        BDDMockito.given(userServiceImpl.findUserById(any(Long.class))).willReturn(userDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(USER_API)
                .with(user(jwtUser))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value(userDto.getId()))
                .andExpect(jsonPath("data.name").value(userDto.getName()))
                .andExpect(jsonPath("data.email").value(userDto.getEmail()));

    }

    @Test
    @DisplayName("Should create a new User.")
    public void shouldCreateNewUserTest() throws Exception {

        UserDto userDto = createUserDto();

        String json = new ObjectMapper().writeValueAsString(userDto);

        User newUser = createUser();

        JwtUser newJwtUser = createJwtUser();

        String token = "Any-token";

        BDDMockito.given(userServiceImpl.createNewUser(any(SignUpDto.class))).willReturn(newUser);

        BDDMockito.given(userDetailsService.loadUserByUsername(newUser.getEmail())).willReturn(newJwtUser);

        BDDMockito.given(jwtTokenUtil.obtainToken(newJwtUser)).willReturn(token);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(USER_API.concat("/sign-up"))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return error for insufficient data.")
    public void shouldCreateUserAndReturnInsufficientData() throws Exception {

        UserDto userDto = createUserDto();

        userDto.setName(null);
        userDto.setPassword(null);
        userDto.setEmail(null);

        String json = new ObjectMapper().writeValueAsString(userDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(USER_API.concat("/sign-up"))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Should update a User by UserDTO.")
    public void shouldUpdateUserTest() throws Exception {

        UserDto userDto = createUserDto();

        String json = new ObjectMapper().writeValueAsString(userDto);

        JwtUser jwtUser = createJwtUser();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(USER_API)
                .with(user(jwtUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value("User updated with success!!!"));
    }

    @Test
    @DisplayName("Should delete a USER by JwtUser.")
    public void shouldDeleteUserTest() throws Exception {

        JwtUser jwtUser = createJwtUser();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(USER_API)
                .with(user(jwtUser))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value("User removed successfully!"));
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("Lucas");
        userDto.setEmail("lucas@email.com");
        userDto.setPassword("123123");

        return userDto;
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

    private JwtUser createJwtUser() {
        User user = createUser();
        return  new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_USER.name())));
    }

}
