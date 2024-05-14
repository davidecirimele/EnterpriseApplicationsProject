package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User getById(Long id);

    User save(User user);

    Optional<User> getByEmail(String email);

    Collection<User> getAll(Specification<User> spec);

    Collection<User> getAll();
    List<UserDto> getUserDto();
}
