package com.enterpriseapplicationsproject.ecommerce.config;

import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.addMappings(new PropertyMap<AddressDto, Address>() {
            @Override
            protected void configure() {
                skip(destination.isDefaultAddress());
                skip(destination.isValid());
            }
        });

        modelMapper.addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                map(source.getCredential().getEmail(), destination.getCredentials().getEmail());
                map(source.getCredential().getPassword(), destination.getCredentials().getPassword());
                map(source.getPhoneNumber(), destination.getPhoneNumber());
                map(source.getAddresses(), destination.getAddresses());
                map(source.getGroups(), destination.getGroups());
            }
        });

        modelMapper.addMappings(new PropertyMap<SaveUserDto, User>() {
            @Override
            protected void configure() {
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getCredentials().getEmail(), destination.getCredential().getEmail());
                map(source.getCredentials().getPassword(), destination.getCredential().getPassword());
                map(source.getPhoneNumber(), destination.getPhoneNumber());
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