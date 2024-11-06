package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.config.EncryptionConfig;
import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.SavePaymentMethodDto;
import com.enterpriseapplicationsproject.ecommerce.exception.*;
import com.enterpriseapplicationsproject.ecommerce.utils.EncryptionUtils;
import com.enterpriseapplicationsproject.ecommerce.data.dao.PaymentMethodsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;

import com.enterpriseapplicationsproject.ecommerce.data.service.PaymentMethodsService;
import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodsServiceImpl implements PaymentMethodsService {

    private final PaymentMethodsDao paymentMethodsDao;

    private final ModelMapper modelMapper;

    private final EncryptionConfig encryptionConfig;

    private final UsersDao userDao;


    @Override
    public PaymentMethodDto addPaymentMethod(SavePaymentMethodDto paymentMethodDto) {
        User user = userDao.findById(paymentMethodDto.getUser().getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        EncryptionUtils encryptionUtils = new EncryptionUtils(encryptionConfig.getSecretKey());
        String encryptedCardNumber;
        try {
            encryptedCardNumber = encryptionUtils.encrypt(paymentMethodDto.getCardNumber());
        }
        catch (Exception e){
            throw  new EncryptionErrorException("Error encrypting card number");
        }
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDto, PaymentMethod.class);
        paymentMethod.setUser(user);
        paymentMethod.setCardNumber(encryptedCardNumber);
        paymentMethod.setValid(true);
        PaymentMethod pm = paymentMethodsDao.save(paymentMethod);
        return modelMapper.map(pm, PaymentMethodDto.class);
    }

    @Override
    public List<PaymentMethodDto> getAllPaymentMethodByUserId(UUID userId) {
        try {
            List<PaymentMethod> paymentMethod = paymentMethodsDao.findAllByUserId(userId);


        EncryptionUtils encryptionUtils = new EncryptionUtils(encryptionConfig.getSecretKey());
        List<PaymentMethodDto> paymentMethodDtoList = paymentMethod.stream().map(pm -> {
            String decryptedCardNumber;
            try {
                decryptedCardNumber = encryptionUtils.decrypt(pm.getCardNumber());
            } catch (Exception e) {
                throw new DecryptionErrorException("Error decrypting card number");
            }
            // Maschera il numero di carta e lo imposta su PaymentMethodDto
            PaymentMethodDto paymentMethodDto = modelMapper.map(pm, PaymentMethodDto.class);
            paymentMethodDto.setCardNumber(MaskCardNumber(decryptedCardNumber));
            return paymentMethodDto;
        }).collect(Collectors.toList());



        return paymentMethodDtoList;
        }
         catch (Exception e){
            throw new PaymentMethodNotFoundException("Payment method not found");
        }

    }

    @Override
    @Transactional
    public void deletePaymentMethodByUserId(UUID userId, Long paymentMethodId) {
        System.out.println("userId: " + userId + " paymentMethodId: " + paymentMethodId);
         Integer res = paymentMethodsDao.setPaymentMethodToFalse(userId, paymentMethodId);
        if (res == 0) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

    }

    @Override
    public PaymentMethodDto getPaymentMethodByUserId(UUID userId, Long paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodsDao.findByUserIdAndPaymentMethodId(userId, paymentMethodId);
        if (paymentMethod == null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        EncryptionUtils encryptionUtils = new EncryptionUtils(encryptionConfig.getSecretKey());
        String decryptedCardNumber;
        try {
            decryptedCardNumber = encryptionUtils.decrypt(paymentMethod.getCardNumber());
        } catch (Exception e) {
            throw new DecryptionErrorException("Error decrypting card number");
        }
        PaymentMethodDto paymentMethodDto = modelMapper.map(paymentMethod, PaymentMethodDto.class);
        paymentMethodDto.setCardNumber(MaskCardNumber(decryptedCardNumber));
        return paymentMethodDto;
    }

    private String MaskCardNumber(String cardNumber) {
        return cardNumber.substring(0, 12) + "****";
    }

}
