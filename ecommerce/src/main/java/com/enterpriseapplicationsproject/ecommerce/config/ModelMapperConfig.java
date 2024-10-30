package com.enterpriseapplicationsproject.ecommerce.config;

import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
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
                map(source.getPhoneNumber(), destination.getPhoneNumber());
            }
        });

        modelMapper.addMappings(new PropertyMap<SaveUserDto, User>() {
            @Override
            protected void configure() {
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getCredential().getEmail(), destination.getCredential().getEmail());
                map(source.getCredential().getPassword(), destination.getCredential().getPassword());
                map(source.getPhoneNumber(), destination.getPhoneNumber());
            }
        });

        modelMapper.addMappings(new PropertyMap<User, UserDetailsDto>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getCredential().getEmail(), destination.getEmail());
                map(source.getPhoneNumber(), destination.getPhoneNumber());
            }
        });

        modelMapper.addMappings(new PropertyMap<Admin, UserDetailsDto>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getCredential().getEmail(), destination.getEmail());
                map(source.getPhoneNumber(), destination.getPhoneNumber());
            }
        });

        modelMapper.addMappings(new PropertyMap<UserDto, User>() {
            @Override
            protected void configure() {
                map(source.getFirstName(), destination.getFirstName());
                map(source.getLastName(), destination.getLastName());
                map(source.getBirthDate(), destination.getBirthDate());
                map(source.getPhoneNumber(), destination.getPhoneNumber());
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

        modelMapper.addMappings(new PropertyMap<ShoppingCart, ShoppingCartDto>() {
            @Override
            protected void configure(){
                map(source.getId(), destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<RefreshTokenDto, RefreshToken>() {
            @Override
            protected void configure(){
                map(source.getToken(), destination.getToken());
            }
        });

        modelMapper.addMappings(new PropertyMap<WishlistDto, Wishlist>() {
            @Override
            protected void configure() {
                map(source.getPrivacySetting(), destination.getPrivacySetting());
                map(source.getWishlistToken(), destination.getWishlistToken());
                map(source.getGroup(), destination.getGroup());
                map(source.getUser(), destination.getUserId());
                map(source.getItems(), destination.getItems());
                map( source.getName(), destination.getName());

            }
        });

        modelMapper.addMappings(new PropertyMap<Wishlist, WishlistDto>() {
            @Override
            protected void configure() {
                map(source.getPrivacySetting(), destination.getPrivacySetting());
                // Aggiungi questa riga per mappare wToken esplicitamente
                map(source.getWishlistToken(), destination.getWishlistToken());
                map(source.getGroup(), destination.getGroup());
                map(source.getUserId(), destination.getUser());
                map(source.getItems(), destination.getItems());
                map(source.getName(), destination.getName());
            }
        });

        modelMapper.typeMap(Order.class, OrderSummaryDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getCredential().getEmail(), OrderSummaryDto::setEmail);
            mapper.map(Order::getOrderId, OrderSummaryDto::setOrderId);
            mapper.map(Order::getOrderDate, OrderSummaryDto::setOrderDate);
            mapper.map(Order::getTotalAmount, OrderSummaryDto::setTotalAmount);
            mapper.map(Order::getOrderStatus, OrderSummaryDto::setOrderStatus);
        });


        return modelMapper;
    }
}