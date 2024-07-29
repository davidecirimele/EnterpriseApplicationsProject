package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersDao userDao;

    private final ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto getById(UUID id) {
        Optional<User> optionalUser = userDao.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return modelMapper.map(user, UserDto.class);
        } else {
            throw new RuntimeException("User with id " + id + " not found");
        }
    }

    @Override
    public UserDto save(User user) {
        User savedUser = userDao.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public UserDto getByEmail(String email) {
        Optional<User> optionalUser = userDao.findByCredentialEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return modelMapper.map(user, UserDto.class);
        } else {
            throw new RuntimeException("User with email " + email + " not found");
        }
    }

    public List<UserDto> getAll() {
        List<User> users = userDao.findAll();
        return users.stream().map(user1 -> modelMapper.map(user1, UserDto.class)).toList();
    }


    @Transactional
    public User getUserById(UUID id) {
        return userDao.findByIdWithAddresses(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    public UserDto updatePassword(PasswordUserDto userDto) {

        User user = userDao.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(userDto.getOldPassword(), user.getCredential().getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }


        user.getCredential().setPassword(passwordEncoder.encode(userDto.getNewPassword()));
        userDao.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    public User convertDto(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        return user;
    }

    @Override
    public UserDto updateEmail(EmailUserDto userDto) {
        User user = userDao.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.getCredential().setEmail(userDto.getNewEmail());
        userDao.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updatePhoneNumber(PhoneNumberUserDto userDto) {
        User user = userDao.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPhoneNumber(userDto.getNewPhoneNumber());
        userDao.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public boolean delete(UserIdDto userId) {
        Optional<User> optionalUser = userDao.findById(userId.getUserId());

        if (optionalUser.isPresent()) {
            userDao.delete(optionalUser.get());
            return true;
        } else {
            throw new RuntimeException("Item with id " + userId.getUserId() + " not found");
        }
    }
}