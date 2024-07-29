package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveAddressDto;

import java.util.List;


public interface AddressService {

    List<AddressDto> getAddressesByUserId(Long userid);

    List<AddressDto> getAddressesByUserIdAndDefaultTrue(Long userid);

    List<AddressDto> getValidAddressesByUserId(Long userid);

    List<AddressDto> getAddresses();

    AddressDto updateAddress(Long addressId, AddressDto addressDto);

    AddressDto save(Address address);

    public SaveAddressDto insertAddress(SaveAddressDto addressDto);

    public AddressDto updateDefaultAddress(AddressIdDto id);

    public boolean deleteAddress(AddressIdDto id);
}
