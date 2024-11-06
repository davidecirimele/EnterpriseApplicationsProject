package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.AddressesDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveAddressDto;
import com.enterpriseapplicationsproject.ecommerce.exception.AddressNotFoundException;
import com.enterpriseapplicationsproject.ecommerce.exception.UnauthorizedAccessException;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressesDao addressesDao;

    private final UsersDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public List<AddressDto> getAddressesByUserId(UUID userid){
        try{
            List<Address> addresses = addressesDao.findByUserId(userid);

            return addresses.stream().map(address -> modelMapper.map(address , AddressDto.class)).toList();
        }catch(Exception e){
            log.error("Unexpected error while fetching addresses by user ID: "+ userid +", "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }

    }

    @Override
    public AddressDto getAddressById(UUID userId, Long addressId) {
        try{
            Address address = addressesDao.findAddressById(addressId).orElseThrow(()->new AddressNotFoundException("Address not found"));

            if(address.getUserId().getId().equals(userId))
                return modelMapper.map(address, AddressDto.class);
            else
                throw new UnauthorizedAccessException("You are not authorized to access this resource");
        }catch(Exception e){
            log.error("Unexpected error while fetching address with ID: "+ addressId +", "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public AddressDto getAddressByUserIdAndDefaultTrue(UUID userid){
        try {
            Address address = addressesDao.findByDefaultAddress(userid).orElse(null);

            if(address == null)
                return null;

            return modelMapper.map(address, AddressDto.class);
        }catch(Exception e){
            log.error("Unexpected error while fetching default address for user with ID: "+ userid +", "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public List<AddressDto> getValidAddressesByUserId(UUID userid){
        try {
            List<Address> addresses = addressesDao.findAllByValidity(userid);
            return addresses.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
        }catch(Exception e){
            log.error("Unexpected error while fetching addresses by user ID: "+ userid +", "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public List<AddressDto> getAddresses(){
        try {
            List<Address> addresses = addressesDao.findAll();
            return addresses.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
        }catch(Exception e){
            log.error("Unexpected error while fetching addresses "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public AddressDto updateAddress(UUID userId, Long addressId, SaveAddressDto addressDto) {
        try {
            Address address = addressesDao.findById(addressId).orElseThrow(()->new AddressNotFoundException(
                    "Address not found"
            ));

            if(address.getUserId().getId().equals(userId)) {
                address.setStreet(addressDto.getStreet());
                address.setState(addressDto.getState());
                address.setProvince(addressDto.getProvince());
                address.setCity(addressDto.getCity());
                address.setPostalCode(addressDto.getPostalCode());
                address.setAdditionalInfo(addressDto.getAdditionalInfo());

                Address savedAddress = addressesDao.save(address);
                return modelMapper.map(savedAddress, AddressDto.class);
            }else{
                throw new UnauthorizedAccessException("You are not authorized to access this resource");
            }

        }catch(DataIntegrityViolationException e){
            log.error("Data integrity violation while updating address with ID: {}", addressId, e);
            throw new IllegalArgumentException("Data integrity violation: " + e.getMessage());
        }
        catch(Exception e){
            log.error("Unexpected error while updating address with ID: "+ addressId +", "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public AddressDto save(Address address) {
        try {
            Address savedAddress = addressesDao.save(address);
            return modelMapper.map(savedAddress, AddressDto.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while saving address: {}", address, e);
            throw new IllegalArgumentException("Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while saving address: {}", address, e);
            throw new RuntimeException("Unexpected error occurred while saving address");
        }
    }

    @Override
    public AddressDto insertAddress(UUID userId, SaveAddressDto addressDto) {
        try {
            User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

            Address address = modelMapper.map(addressDto, Address.class);
            address.setUser(user);

            List<Address> addressess = addressesDao.findAllByValidity(userId);

            if (addressess.isEmpty())
                address.setDefaultAddress(true);

            address.setIsValidAddress(true);

            Address savedAddress = addressesDao.save(address);
            return modelMapper.map(savedAddress, AddressDto.class);
        }catch(Exception e){
            log.error("Unexpected error: " + e);
            throw new RuntimeException("Unexpected error occurred while saving address");
        }
    }

    @Override
    public AddressDto updateDefaultAddress(UUID userId, Long id){
        try {
            Address address = addressesDao.findById(id).orElseThrow(()->new AddressNotFoundException("Address not found"));

            if(address.getUserId().getId().equals(userId)) {
                if (address.isValid()) {
                    List<Address> addresses = new ArrayList<>();

                    Address defaultAddress = addressesDao.findByDefaultAddress(addressesDao.findUserByAddressId(id).getId()).orElseThrow(() ->
                            new AddressNotFoundException("Address not found"));

                    defaultAddress.setDefaultAddress(false);
                    address.setDefaultAddress(true);

                    addresses.add(defaultAddress);
                    addresses.add(address);

                    addressesDao.saveAll(addresses);

                    return modelMapper.map(address, AddressDto.class);
                } else {
                    throw new AddressNotFoundException("Address with id " + id + " not found");
                }
            }else{
                throw new UnauthorizedAccessException("You are not authorized to access this resource");
            }
        }catch(Exception e){
            log.error("Unexpected error while updating default address: " + e);
            throw new RuntimeException("Unexpected error occurred while updating default address");
        }

    }

    @Override
    public void deleteAddress(UUID userId, Long id) {
        try {
            Address address = addressesDao.isValidByAddressId(id).orElseThrow(()->new AddressNotFoundException("Address not found"));

            if(address.getUserId().getId().equals(userId)) {
                address.setIsValidAddress(false);
                if (address.isDefaultAddress())
                    assignNewDefault(userId, id);
                address.setDefaultAddress(false);

                addressesDao.save(address);
            }else{
                throw new UnauthorizedAccessException("You are not authorized to access this resource");
            }
        }catch(Exception e){
            log.error("Unexpected error while deleting address with ID: {}", id, e);
            throw new RuntimeException("Unexpected error occurred while deleting address");
        }
    }

    private void assignNewDefault(UUID userId, Long id){
        try {
            List<Address> valid_addresses = addressesDao.findAllByValidity(addressesDao.findUserByAddressId(id).getId());

            if (valid_addresses.isEmpty())
                throw new AddressNotFoundException("Addresses not found");

            for (Address a : valid_addresses) {
                if (!a.getId().equals(id)) {
                    updateDefaultAddress(userId, a.getId());
                    return;
                }
            }
        }catch(Exception e){
            log.error("Unexpected error while assigning new default address" + e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }
}
