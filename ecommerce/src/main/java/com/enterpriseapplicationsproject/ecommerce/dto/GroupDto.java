package com.enterpriseapplicationsproject.ecommerce.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class GroupDto {

    private Long id;
    private String groupName;
    private List<UserDto> members = new ArrayList<>();
}
