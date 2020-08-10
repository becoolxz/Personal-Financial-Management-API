package br.com.lucas.study.personalfinancialmanagementapi.security.services;

import br.com.lucas.study.personalfinancialmanagementapi.model.User;
import br.com.lucas.study.personalfinancialmanagementapi.security.JwtUserFactory;
import br.com.lucas.study.personalfinancialmanagementapi.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceImpl userServiceImpl;

    public JwtUserDetailsServiceImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userServiceImpl.findUserByUsernameEmail(email);

        return JwtUserFactory.create(user);
    }

}
