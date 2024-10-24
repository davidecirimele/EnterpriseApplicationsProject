package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.ResourceNotFoundException;
import com.enterpriseapplicationsproject.ecommerce.exception.UserAlreadyExistsException;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersDao userDao;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

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
    public UserDetailsDto getUserDetailsById(UUID id) {
        Optional<User> optionalUser = userDao.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return modelMapper.map(user, UserDetailsDto.class);
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

    public List<UserDetailsDto> getAllDto() {
        List<User> users = userDao.findAll();
        return users.stream().map(user1 -> modelMapper.map(user1, UserDetailsDto.class)).toList();
    }

    public List<User> getAll() {
        return userDao.findAll();
    }


    @Transactional
    public User getUserById(UUID id) {
        return userDao.findByIdWithAddresses(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userDao.findByCredentialEmail(email);

        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        return optionalUser.get();
    }


    public UserDto updatePassword(UUID userId, PasswordUserDto userDto) {
        User user = userDao.findById(userId)
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
    public UserDto updateEmail(UUID userId, EmailUserDto userDto) {

        userDao.findByCredentialEmail(userDto.getNewEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this email already exists");
        });

        User user = userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.getCredential().setEmail(userDto.getNewEmail());
        userDao.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updatePhoneNumber(UUID userId, PhoneNumberUserDto userDto) {

        userDao.findByPhoneNumber(userDto.getNewPhoneNumber()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this phone number already exists");
        });

        User user = userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPhoneNumber(userDto.getNewPhoneNumber());
        userDao.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public boolean delete(UUID userId) {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isPresent()) {
            userDao.delete(optionalUser.get());
            return true;
        } else {
            throw new RuntimeException("Item with id " + userId + " not found");
        }
    }

    @Override
    public String getUserRole(UUID userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getClass().getAnnotation(DiscriminatorValue.class).value();
    }
}