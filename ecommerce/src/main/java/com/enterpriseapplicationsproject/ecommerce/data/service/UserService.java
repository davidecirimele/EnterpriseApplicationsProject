package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

public interface UserService {

    UserDto getById(Long id);

    UserDto save(User user);

    UserDto getByEmail(String email);

    List<UserDto> getAll();

    @Transactional
    User getUserById(Long id);

    UserDto addUser(UserDto userDto);

    User convertDto(UserDto userDto);
}
