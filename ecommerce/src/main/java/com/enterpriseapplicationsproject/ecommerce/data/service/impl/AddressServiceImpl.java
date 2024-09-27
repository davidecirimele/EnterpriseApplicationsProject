package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.AddressesDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveAddressDto;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressesDao addressesDao;

    private final UsersDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public List<AddressDto> getAddressesByUserId(UUID userid){
        List<Address> addresses = addressesDao.findByUserId(userid);
        return addresses.stream().map(address -> modelMapper.map(address , AddressDto.class)).toList();
    }

    @Override
    public AddressDto getAddressById(Long addressId) {
        Optional<Address> optionalAddress = addressesDao.findAddressById(addressId);

        if(optionalAddress.isPresent()){
            Address address = optionalAddress.get();

            return modelMapper.map(address, AddressDto.class);
        }
        else{
            throw new RuntimeException("Address with id " + addressId + " not found");
        }


    }

    @Override
    public AddressDto getAddressByUserIdAndDefaultTrue(UUID userid){
        Address address = addressesDao.findByDefaultAddress(userid);
        return modelMapper.map(address , AddressDto.class);
    }

    @Override
    public List<AddressDto> getValidAddressesByUserId(UUID userid){
        List<Address> addresses = addressesDao.findAllByValidity(userid);
        return addresses.stream().map(address -> modelMapper.map(address , AddressDto.class)).toList();
    }

    @Override
    public List<AddressDto> getAddresses(){
        List<Address> addresses = addressesDao.findAll();
        return addresses.stream().map(address -> modelMapper.map(address , AddressDto.class)).toList();
    }

    @Override
    public AddressDto updateAddress(Long addressId, SaveAddressDto addressDto) {

        Optional<Address> optionalAddress = addressesDao.findById(addressId);

        if(optionalAddress.isPresent())
        {
            Address address = optionalAddress.get();
            address.setStreet(addressDto.getStreet());
            address.setState(addressDto.getState());
            address.setProvince(addressDto.getProvince());
            address.setCity(addressDto.getCity());
            address.setPostalCode(addressDto.getPostalCode());
            address.setAdditionalInfo(addressDto.getAdditionalInfo());

            Address savedAddress = addressesDao.save(address);
            return modelMapper.map(savedAddress, AddressDto.class);
        }
        else{
            throw new RuntimeException("Address with id " + addressId + " not found");
        }
    }

    @Override
    public AddressDto save(Address address) {
        Address savedAddress = addressesDao.save(address);
        return modelMapper.map(savedAddress, AddressDto.class);
    }

    @Override
    public SaveAddressDto insertAddress(UUID userId, SaveAddressDto addressDto) {
        User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = modelMapper.map(addressDto, Address.class);
        address.setUser(user);
        List<Address> addressess = addressesDao.findAllByValidity(userId);
        if(addressess.isEmpty())
            address.setDefaultAddress(true);
        address.setIsValidAddress(true);
        Address savedAddress = addressesDao.save(address);
        return modelMapper.map(savedAddress, SaveAddressDto.class);
    }

    @Override
    public AddressDto updateDefaultAddress(Long id){
        Optional<Address> optionalAddress = addressesDao.findById(id);

        if(optionalAddress.isPresent() && optionalAddress.get().isValid())
        {
            Address address = optionalAddress.get();

            List<Address> addresses = new ArrayList<>();

            Address defaultAddress = addressesDao.findByDefaultAddress(addressesDao.findUserByAddressId(id).getId());

            defaultAddress.setDefaultAddress(false);
            address.setDefaultAddress(true);

            addresses.add(defaultAddress);
            addresses.add(address);

            addressesDao.saveAll(addresses);

            return modelMapper.map(address, AddressDto.class);
        }
        else{
            throw new RuntimeException("Address with id " + id + " not found");
        }

    }

    @Override
    public boolean deleteAddress(Long id) {
        Optional<Address> optionalAddress = addressesDao.isValidByAddressId(id);
        if(optionalAddress.isPresent())
        {
            Address address = optionalAddress.get();
            address.setIsValidAddress(false);
            if(address.isDefaultAddress())
                assignNewDefault(id);
            address.setDefaultAddress(false);


            addressesDao.save(address);
            return true;
        }
        else{
            throw new RuntimeException("Address with id " + id + " not found");
        }
    }

    private void assignNewDefault(Long id){
        List<Address> valid_addresses = addressesDao.findAllByValidity(addressesDao.findUserByAddressId(id).getId());

        if(valid_addresses.isEmpty())
            return;

        for(Address a:valid_addresses){
            if(!a.getId().equals(id)) {
                updateDefaultAddress(a.getId());
                return;
            }
        }
    }
}
