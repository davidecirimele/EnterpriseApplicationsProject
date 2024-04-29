package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersDao extends JpaRepository<User, Long> {

    //Visualizzare lista indirizzi associati
    List<Address> findAddressesById(Long userId);

    //Visualizzare lista persone con cui ha condiviso una lista dei desideri

    //Visualizzare lista dei desideri

    //Visualizzare carrello associato

    //Visualizzare lista dei prodotti

    //Modificare numero di telefono associato

    //Rimuovere un indirizzo associato

    //Aggiungere un indirizzo

    //Modificare la password

    //Scegliere indirizzo di default

    //Cambiare immagine del profilo

    //Eliminare il proprio Account
}
