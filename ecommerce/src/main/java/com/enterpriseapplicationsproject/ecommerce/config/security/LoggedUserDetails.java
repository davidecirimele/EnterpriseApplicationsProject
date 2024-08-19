package com.enterpriseapplicationsproject.ecommerce.config.security;


import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data


public class LoggedUserDetails implements UserDetails{

    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<GrantedAuthority> authorities;

    public LoggedUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getCredential().getEmail();
        this.password = user.getCredential().getPassword();
        this.authorities = Collections.singletonList( new SimpleGrantedAuthority("ROLE_" + user.getClass().getAnnotation(DiscriminatorValue.class).value()));
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}