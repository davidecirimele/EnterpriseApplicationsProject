package com.enterpriseapplicationsproject.ecommerce.config;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static com.enterpriseapplicationsproject.ecommerce.data.entities.User_.addresses;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                map(source.getCredential().getEmail(), destination.getCredentials().getEmail());
                map(source.getProfilePicture(), destination.getProfilePicture());
                map(source.getPhoneNumber(), destination.getPhoneNumber());
                map(source.getAddresses(), destination.getAddresses());
                map(source.getGroups(), destination.getGroups());
            }
        });

        modelMapper.addMappings(new PropertyMap<UserDto, User>() {
            @Override
            protected void configure() {
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getCredentials().getEmail(), destination.getCredential().getEmail());
                map(source.getCredentials().getPassword(), destination.getCredential().getPassword());
                map(source.getBirthDate(), destination.getBirthDate());
                map(source.getProfilePicture(), destination.getProfilePicture());
                map(source.getPhoneNumber(), destination.getPhoneNumber());
                map(source.getAddresses(), destination.getAddresses());
                map(source.getGroups(), destination.getGroups());
            }
        });

        modelMapper.addMappings(new PropertyMap<Order, OrderDto>(){
            @Override
            protected void configure() {
                map().getAddress().setStreet(source.getAddress().getStreet());
            }

        });

        modelMapper.addMappings(new PropertyMap<PaymentMethodDto, PaymentMethod>() {
            @Override
            protected void configure(){
                skip(destination.getCardNumber());
            }
        });


        return modelMapper;
    }
}