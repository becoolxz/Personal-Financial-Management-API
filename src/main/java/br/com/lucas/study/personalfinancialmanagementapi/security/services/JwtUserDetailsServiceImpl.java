package br.com.lucas.study.personalfinancialmanagementapi.security.services;

import br.com.lucas.study.personalfinancialmanagementapi.model.User;
import br.com.lucas.study.personalfinancialmanagementapi.security.JwtUserFactory;
import br.com.lucas.study.personalfinancialmanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userService.findUserByUsernameEmail(email);

        if (user.isPresent()) {
            return JwtUserFactory.create(user.get());
        }

        throw new UsernameNotFoundException("Email not found.");
    }

}
