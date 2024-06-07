package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public User getById(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userDao.findByCredentialEmail(email);
    }

    @Override
    public Collection<User> getAll(Specification<User> spec) {
        //return userDao.findAll(spec);
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return userDao.findAll();
    }

    @Override
    public List<UserDto> getUserDto() {
        if(userDao == null) {
            System.out.println("userDao is null!");
            throw new NullPointerException();
        }
        List<User> users = userDao.findAll();
        if(users == null || users.isEmpty()) {
            System.out.println("No users found!");
        } else {
            System.out.println("Users found: " + users.size());
        }
        return users.stream()
                .map(s -> modelMapper.map(s, UserDto.class))
                .collect(Collectors.toList());
    }
}
