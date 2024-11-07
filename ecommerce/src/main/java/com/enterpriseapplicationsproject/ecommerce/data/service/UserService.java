package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto getById(UUID id);

    UserDto save(User user);

    List<UserDetailsDto> getAllDto();

    UserDetailsDto getUserDetailsById(UUID id);

    User getUserById(UUID id);


    UserDto updatePassword(UUID userId, PasswordUserDto userDto);

    UserDto updateEmail(UUID userId, EmailUserDto userDto);

    UserDto updatePhoneNumber(UUID userId, PhoneNumberUserDto userDto);

    boolean delete(UUID userId);

    List<User> getAll();
}
