package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersistentBagToListConverter implements Converter<PersistentBag, List<?>> {
    @Override
    public List<?> convert(MappingContext<PersistentBag, List<?>> context) {
        PersistentBag source = context.getSource();
        if (source != null) {
            return new ArrayList<>(source);
        } else {
            return Collections.emptyList();
        }
    }
}