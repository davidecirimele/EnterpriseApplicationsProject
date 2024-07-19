package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private byte[] profilePicture;

    private CredentialDto credentials;

    private List<AddressDto> addresses;

    private String phoneNumber;

    private List<Group> groups;

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public CredentialDto getCredentials(){
        return credentials;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }
    public void setAddresses(List<Address> addresses) {
        ModelMapper modelMapper = new ModelMapper();
        if(addresses != null)
            this.addresses = modelMapper.map(addresses, new TypeToken<List<AddressDto>>() {}.getType());
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }


}
