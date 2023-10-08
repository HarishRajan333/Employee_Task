package com.mycompany.employee.Security;

import com.mycompany.employee.Model.Roles;
import com.mycompany.employee.Model.UserCredientails;
import com.mycompany.employee.Repository.UserCredientailsRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Autowired
    UserCredientailsRepository userCredRepo;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserCredientails userCred = userCredRepo.findByUsername(username);

        if (Objects.isNull(userCred)) {
            throw new BadCredentialsException("User not Found");
        }

        if (encoder.matches(password, userCred.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userCred, username, extractRoles(userCred.getEmployee().getRoles()));
        } else {
            throw new BadCredentialsException("username or password wrong");
        }

    }

    public List<GrantedAuthority> extractRoles(List<Roles> roles) {
        List<GrantedAuthority> auth = roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleName())).collect(Collectors.toList());
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
