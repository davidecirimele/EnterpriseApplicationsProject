package com.enterpriseapplicationsproject.ecommerce.config;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;

import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.Converters.Collection.map;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.createTypeMap(User.class, UserDto.class).addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                // define a converter that takes the whole "person"
                using(ctx -> generateFullname(((User) ctx.getSource()).getFirstName(), ((User) ctx.getSource()).getLastName()))
                        // Map the compliete source here
                        .map(source, destination.getFullName());
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

    private String generateFullname(String firstname, String lastname) {
        return firstname + " " + lastname;
    }
}
