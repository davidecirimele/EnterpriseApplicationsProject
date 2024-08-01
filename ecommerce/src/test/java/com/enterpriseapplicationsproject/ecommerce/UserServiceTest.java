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

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UsersDao usersDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        SaveUserDto userDto = new SaveUserDto();
        userDto.setFirstName("Dado");
        userDto.setLastName("Ciri");
        userDto.setBirthDate(LocalDate.parse("1998-03-05"));
        userDto.getCredentials().setEmail("davi598@gmail.com");
        userDto.getCredentials().setPassword("password123");
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
        SaveUserDto createdUser = authService.registerUser(userDto);

        // Assert
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getCredential().getEmail(), createdUser.getCredentials().getEmail());
    }

}
