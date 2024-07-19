package com.enterpriseapplicationsproject.ecommerce.config.security;

import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class LoggedUserDetailsService implements UserDetailsService {

    private UserDetails loadUser;

    private final UsersDao usersDao;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usersDao.findByCredentialEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        LoggedUserDetails userDetails = new LoggedUserDetails(user, user.get);

    }
}
