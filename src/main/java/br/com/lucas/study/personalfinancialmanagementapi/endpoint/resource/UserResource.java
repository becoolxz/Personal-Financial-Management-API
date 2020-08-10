package br.com.lucas.study.personalfinancialmanagementapi.endpoint.resource;

import br.com.lucas.study.personalfinancialmanagementapi.model.User;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.SignUpDto;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.TokenDto;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.UserDto;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.response.Response;
import br.com.lucas.study.personalfinancialmanagementapi.security.JwtUser;
import br.com.lucas.study.personalfinancialmanagementapi.security.utils.JwtTokenUtil;
import br.com.lucas.study.personalfinancialmanagementapi.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/users")
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    private final UserServiceImpl userServiceImpl;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;

    public UserResource(UserServiceImpl userServiceImpl, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.userServiceImpl = userServiceImpl;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public ResponseEntity<Response<UserDto>> getUser(@AuthenticationPrincipal JwtUser jwtUser) {

        LOGGER.info("Searching User data by ID: " +  jwtUser.getId());

        Response<UserDto> response = new Response<>();

        UserDto userDto = userServiceImpl.findUserById(jwtUser.getId());

        response.setData(userDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response<TokenDto>> save(@Valid @RequestBody SignUpDto signUpDto, BindingResult result) {

        Response<TokenDto> response = new Response<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        User user = userServiceImpl.createNewUser(signUpDto);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtTokenUtil.obtainToken(userDetails);

        response.setData(new TokenDto(token));

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Response<String>> update(@Valid @RequestBody UserDto userDto,
                                                   BindingResult result) {

        LOGGER.info("Updating user: {}", userDto.toString());
        Response<String> response = new Response<>();

        if (result.hasErrors()) {
            LOGGER.error("Erro validando usuário: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.userServiceImpl.update(userDto);

        response.setData("User updated with success!!!");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Response<String>> delete(@AuthenticationPrincipal JwtUser jwtUser) {

        LOGGER.info("Removing user ID: {}", jwtUser.getId());

        Response<String> response = new Response<>();

        this.userServiceImpl.delete(jwtUser.getId());

        response.setData("User removed successfully!");

        return ResponseEntity.ok(response);
    }

}
