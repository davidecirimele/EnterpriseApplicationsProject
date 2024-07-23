package com.enterpriseapplicationsproject.ecommerce.config.security;

import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoggedUserDetailsService implements UserDetailsService {

    private  UsersDao usersDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usersDao.findByCredentialEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("User: " + user);
        return new LoggedUserDetails(user);

    }

}
