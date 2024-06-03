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
    public UserDto getById(Long id) {
        Optional<User> optionalUser = userDao.findById(id);

        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            return modelMapper.map(user, UserDto.class);
        }
        else{
            throw new RuntimeException("User with id " + id + " not found");
        }
    }

    @Override
    public UserDto save(User user) {
        User savedUser = userDao.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getByEmail(String email) {
        Optional<User> optionalUser = userDao.findByCredentialEmail(email);

        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            return modelMapper.map(user, UserDto.class);
        }
        else{
            throw new RuntimeException("User with email " + email + " not found");
        }

    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userDao.findAll();
        return users.stream().map(user1 -> modelMapper.map(user1 , UserDto.class)).toList();
    }
}
