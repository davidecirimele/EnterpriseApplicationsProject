package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.PaymentMethodsDao;
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


    @Override
    public PaymentMethodDto addPaymentMethod(PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDto, PaymentMethod.class);
        PaymentMethod pm = paymentMethodsDao.save(paymentMethod);
        return modelMapper.map(pm, PaymentMethodDto.class);
    }

    @Override
    public PaymentMethodDto getPaymentMethodByUserId(Long userId) {
        PaymentMethod paymentMethod = paymentMethodsDao.findByUserId(userId);
        return modelMapper.map(paymentMethod, PaymentMethodDto.class);
    }

    @Override
    public PaymentMethodDto deletePaymentMethodByUserId(Long userId) {
        PaymentMethod paymentMethod = paymentMethodsDao.deleteByUserId(userId);
        return modelMapper.map(paymentMethod, PaymentMethodDto.class);
    }





}
