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

    UserDto getByEmail(String email);

    List<UserDto> getAll();

    @Transactional
    User getUserById(UUID id);

    User getUserByEmail(String email);

    UserDto updatePassword(PasswordUserDto userDto);

    User convertDto(UserDto userDto);

    UserDto updateEmail(EmailUserDto userDto);

    UserDto updatePhoneNumber(PhoneNumberUserDto userDto);

    boolean delete(UserIdDto userId);

    String getUserRole(UUID userId);
}
