package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.AddressesDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveAddressDto;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public List<AddressDto> getAddressesByUserIdAndDefaultTrue(UUID userid){
        List<Address> addresses = addressesDao.findAllByDefaultAddress(userid);
        return addresses.stream().map(address -> modelMapper.map(address , AddressDto.class)).toList();
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
    public AddressDto updateAddress(Long addressId, AddressDto addressDto) {

        Optional<Address> optionalAddress = addressesDao.findById(addressId);

        if(optionalAddress.isPresent())
        {
            Address address = optionalAddress.get();
            address.setDefaultAddress(addressDto.isDefaultAddress());

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
    public SaveAddressDto insertAddress(SaveAddressDto addressDto) {
        userDao.findById(addressDto.getUser().getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));


        Address address = modelMapper.map(addressDto, Address.class);
        List<Address> addressess = addressesDao.findAllByValidity(addressDto.getUser().getUserId());
        if(addressess.isEmpty())
            address.setDefaultAddress(true);
        address.setIsValidAddress(true);
        Address savedAddress = addressesDao.save(address);
        return modelMapper.map(savedAddress, SaveAddressDto.class);
    }

    @Override
    public AddressDto updateDefaultAddress(AddressIdDto id){
        Optional<Address> optionalAddress = addressesDao.findById(id.getAddressId());

        if(optionalAddress.isPresent())
        {
            Address address = optionalAddress.get();

            if(address.isDefaultAddress())
                address.setDefaultAddress(false);
            else{
                List<Address> defaults = addressesDao.findAllByDefaultAddress(addressesDao.findUserByAddressId(id.getAddressId()).getId());

                for (Address a : defaults){
                    if(a.isDefaultAddress()) {
                        a.setDefaultAddress(false);
                        addressesDao.save(a);
                        break;
                    }
                }

                address.setDefaultAddress(true);
            }

            Address savedAddress = addressesDao.save(address);
            return modelMapper.map(savedAddress, AddressDto.class);
        }
        else{
            throw new RuntimeException("Address with id " + id.getAddressId() + " not found");
        }

    }

    @Override
    public boolean deleteAddress(AddressIdDto id) {
        Optional<Address> optionalAddress = addressesDao.isValidByAddressId(id.getAddressId());
        if(optionalAddress.isPresent())
        {
            Address address = optionalAddress.get();
            address.setIsValidAddress(false);
            if(address.isDefaultAddress())
                assignNewDefault(id);
            address.setDefaultAddress(false);


            AddressDto newAddressDto = modelMapper.map(address, AddressDto.class);
            updateAddress(id.getAddressId(), newAddressDto);
            return true;
        }
        else{
            throw new RuntimeException("Address with id " + id.getAddressId() + " not found");
        }
    }

    private void assignNewDefault(AddressIdDto id){
        List<Address> valid_addresses = addressesDao.findAllByValidity(addressesDao.findUserByAddressId(id.getAddressId()).getId());

        for(Address a:valid_addresses){
            if(!a.getId().equals(id.getAddressId())) {
                updateDefaultAddress(id);
                return;
            }
        }
    }
}
