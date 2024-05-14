package com.enterpriseapplicationsproject.ecommerce.config;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

        return modelMapper;
    }

    private String generateFullname(String firstname, String lastname) {
        return firstname + " " + lastname;
    }
}
