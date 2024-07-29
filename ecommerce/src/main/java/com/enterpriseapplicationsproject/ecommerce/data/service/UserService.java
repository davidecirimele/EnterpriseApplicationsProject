package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDto getById(Long id);

    UserDto save(User user);

    UserDto getByEmail(String email);

    List<UserDto> getAll();

    @Transactional
    User getUserById(Long id);

    //UserDto addUser(UserDto userDto, MultipartFile file);

    UserDto addUser(UserDto userDto);

    UserDto updatePassword(PasswordUserDto userDto);

    User convertDto(UserDto userDto);

    UserDto updateEmail(EmailUserDto userDto);

    UserDto updatePhoneNumber(PhoneNumberUserDto userDto);

    boolean delete(UserIdDto userId);
}
