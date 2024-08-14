package com.enterpriseapplicationsproject.ecommerce;

import com.enterpriseapplicationsproject.ecommerce.config.ModelMapperConfig;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AuthService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.data.service.impl.AuthServiceImpl;
import com.enterpriseapplicationsproject.ecommerce.data.service.impl.UserServiceImpl;
import com.enterpriseapplicationsproject.ecommerce.dto.CredentialDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveUserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDetailsDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    private UserServiceImpl userService;
    private AuthServiceImpl authService;

    private UsersDao usersDao;


    @Test
    public void testCreateUser() {
        SaveUserDto userDto = new SaveUserDto();
        userDto.setFirstName("Dado");
        userDto.setLastName("Ciri");
        userDto.setBirthDate(LocalDate.parse("1998-03-05"));
        userDto.getCredential().setEmail("davi598@gmail.com");
        userDto.getCredential().setPassword("password123");
        userDto.setPhoneNumber("3462819839");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Dado");
        user.setLastName("Ciri");
        user.getCredential().setEmail("dadociri7@example.com");


        String encryptedPassword = passwordEncoder.encode("password123");
        user.getCredential().setPassword(encryptedPassword);

        when(modelMapper.map(any(SaveUserDto.class), any(Class.class))).thenReturn(user);

        // Act
        UserDetailsDto createdUser = authService.registerUser(userDto);

        // Assert
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getCredential().getEmail(), createdUser.getEmail());
    }

    @Test
    public void testChangeUserPassword() {
        SaveUserDto userDto = new SaveUserDto();
        userDto.setFirstName("Dado");
        userDto.setLastName("Ciri");
        userDto.setBirthDate(LocalDate.parse("1998-03-05"));
        userDto.getCredential().setEmail("davi598@gmail.com");
        userDto.getCredential().setPassword("password123");
        userDto.setPhoneNumber("3462819839");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Dado");
        user.setLastName("Ciri");
        user.getCredential().setEmail("dadociri7@example.com");


        String encryptedPassword = passwordEncoder.encode("password123");
        user.getCredential().setPassword(encryptedPassword);

        when(modelMapper.map(any(SaveUserDto.class), any(Class.class))).thenReturn(user);

        // Act
        UserDetailsDto createdUser = authService.registerUser(userDto);

        // Assert
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getCredential().getEmail(), createdUser.getEmail());
    }

    @Test
    public void testChangeUserEmail() {
        SaveUserDto userDto = new SaveUserDto();
        userDto.setFirstName("Dado");
        userDto.setLastName("Ciri");
        userDto.setBirthDate(LocalDate.parse("1998-03-05"));
        userDto.getCredential().setEmail("davi598@gmail.com");
        userDto.getCredential().setPassword("password123");
        userDto.setPhoneNumber("3462819839");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Dado");
        user.setLastName("Ciri");
        user.getCredential().setEmail("dadociri7@example.com");


        String encryptedPassword = passwordEncoder.encode("password123");
        user.getCredential().setPassword(encryptedPassword);

        when(modelMapper.map(any(SaveUserDto.class), any(Class.class))).thenReturn(user);

        // Act
        // Act
        UserDetailsDto createdUser = authService.registerUser(userDto);

        // Assert
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getCredential().getEmail(), createdUser.getEmail());
    }

    @Test
    public void testChangeUserPhoneNumber() {
        SaveUserDto userDto = new SaveUserDto();
        userDto.setFirstName("Dado");
        userDto.setLastName("Ciri");
        userDto.setBirthDate(LocalDate.parse("1998-03-05"));
        userDto.getCredential().setEmail("davi598@gmail.com");
        userDto.getCredential().setPassword("password123");
        userDto.setPhoneNumber("3462819839");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Dado");
        user.setLastName("Ciri");
        user.getCredential().setEmail("dadociri7@example.com");


        String encryptedPassword = passwordEncoder.encode("password123");
        user.getCredential().setPassword(encryptedPassword);

        when(modelMapper.map(any(SaveUserDto.class), any(Class.class))).thenReturn(user);

        // Act
        UserDetailsDto createdUser = authService.registerUser(userDto);

        // Assert
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getCredential().getEmail(), createdUser.getEmail());
    }



}
