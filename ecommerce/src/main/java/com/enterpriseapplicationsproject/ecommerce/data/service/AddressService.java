package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.EditAddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveAddressDto;

import java.util.List;
import java.util.UUID;


public interface AddressService {

    List<AddressDto> getAddressesByUserId(UUID userid);

    AddressDto getAddressById(Long addressId);

    AddressDto getAddressByUserIdAndDefaultTrue(UUID userid);

    List<AddressDto> getValidAddressesByUserId(UUID userid);

    List<AddressDto> getAddresses();

    AddressDto updateAddress(EditAddressDto addressDto);

    AddressDto save(Address address);

    public SaveAddressDto insertAddress(SaveAddressDto addressDto);

    public AddressDto updateDefaultAddress(Long id);

    public boolean deleteAddress(AddressIdDto id);
}
