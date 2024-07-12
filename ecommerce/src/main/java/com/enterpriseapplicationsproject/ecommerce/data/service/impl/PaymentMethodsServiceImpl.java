package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.config.EncryptionConfig;
import com.enterpriseapplicationsproject.ecommerce.utils.EncryptionUtils;
import com.enterpriseapplicationsproject.ecommerce.data.dao.PaymentMethodsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import com.enterpriseapplicationsproject.ecommerce.data.service.PaymentMethodsService;
import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMethodsServiceImpl implements PaymentMethodsService {

    private final PaymentMethodsDao paymentMethodsDao;

    private final ModelMapper modelMapper;

    private final EncryptionConfig encryptionConfig;

    private final UsersDao userDao;


    @Override
    public PaymentMethodDto addPaymentMethod(PaymentMethodDto paymentMethodDto) {

        System.out.println("PaymentMethodDto: " + paymentMethodDto.toString());

        /*User user = userDao.getById(paymentMethodDto.getUser().getId());
        if (user == null){
            throw  new IllegalArgumentException("User not found");
        }*/

        EncryptionUtils encryptionUtils = new EncryptionUtils(encryptionConfig.getSecretKey());
        String encryptedCardNumber;
        try {
            encryptedCardNumber = encryptionUtils.encrypt(paymentMethodDto.getCardNumber());
        }
        catch (Exception e){
            throw  new RuntimeException("Error encvrypting card number", e);
        }
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDto, PaymentMethod.class);
        System.out.println("PaymentMethod: " + paymentMethod.toString());
        paymentMethod.setCardNumber(encryptedCardNumber);
        PaymentMethod pm = paymentMethodsDao.save(paymentMethod);
        return modelMapper.map(pm, PaymentMethodDto.class);
    }

    @Override
    public PaymentMethodDto getPaymentMethodByUserId(Long userId) {
        PaymentMethod paymentMethod = paymentMethodsDao.findByUserId(userId);
        EncryptionUtils encryptionUtils = new EncryptionUtils(encryptionConfig.getSecretKey());
        String decryptedCardNumber;
        try {
            decryptedCardNumber = encryptionUtils.decrypt(paymentMethod.getCardNumber());
        }
        catch (Exception e){
            throw  new RuntimeException("Error decrypting card number", e);
        }
        PaymentMethodDto paymentMethodDto = modelMapper.map(paymentMethod, PaymentMethodDto.class);
        paymentMethodDto.setCardNumber(MaskCardNumber(decryptedCardNumber));

        return paymentMethodDto;
    }

    @Override
    public PaymentMethodDto deletePaymentMethodByUserId(Long userId) {
        PaymentMethod paymentMethod = paymentMethodsDao.deleteByUserId(userId);
        return modelMapper.map(paymentMethod, PaymentMethodDto.class);
    }

    private String MaskCardNumber(String cardNumber) {
        return cardNumber.substring(0, 12) + "****";
    }

}
