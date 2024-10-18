package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.SaveAddress

import com.example.ecommercefront_end.network.CheckoutApiService
import retrofit2.Response
import java.util.UUID

class CheckoutRepository(private val checkoutApiService: CheckoutApiService) {

    // Recupera l'indirizzo di spedizione
    suspend fun getShippingAddress(userId: UUID) : Response<Address?> {
        return  checkoutApiService.getDefaultAddressByUserId(userId)

    }

    suspend fun getShippingAddresses(userId: UUID) : Response<List<Address>?> {
        return checkoutApiService.getShippingAddresses(userId)
    }

    // Recupera la lista dei metodi di pagamento
    suspend fun getPaymentMethods(userId: Long){
        return checkoutApiService.getPaymentMethods(userId)
    }

    // Recupera il totale dell'ordine
    suspend fun getOrderTotal(userId: UUID): Double {
        return checkoutApiService.getOrderTotal(userId)
    }

    // Aggiorna l'indirizzo di spedizione
    suspend fun addShippingAddress(address: SaveAddress, sessionManager: SessionManager) : Address? {
        val user = sessionManager.user?.id
        if (user != null) {
            val newAdd = checkoutApiService.insertShippingAddress(user, address)
            return newAdd
        }
        return null
    }

    suspend fun updateShippingAddress(address: SaveAddress, sessionManager: SessionManager, addressId: Long) : Address? {
        val user = sessionManager.user?.id
        if (user != null) {
            val newAdd = checkoutApiService.updateShippingAddress(user, addressId, address)
            return newAdd
        }
        return null

    }

    // Aggiungi un nuovo metodo di pagamento
    /*suspend fun addPaymentMethod(userId: Long, paymentMethod: SavePaymentMethod): List<PaymentMethod> {
        return checkoutApiService.addPaymentMethod(userId, paymentMethod)
    }*/
}
