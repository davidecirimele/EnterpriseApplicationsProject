package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.CheckoutRequest
import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.SaveOrder
import com.example.ecommercefront_end.model.SavePaymentMethod

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
    suspend fun getPaymentMethods(userId: UUID) : Response<List<PaymentMethod>?> {
        return checkoutApiService.getPaymentMethods(userId)
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

    suspend fun addPaymentMethod(paymentMethod: SavePaymentMethod): Response<PaymentMethod> {
        println("metodo di pagamento che sto salvando:$paymentMethod")
        return checkoutApiService.addPaymentMethod(paymentMethod)
    }

    suspend fun deletePaymentMethod(userId: UUID, paymentMethodId: Long) {
        println("metodo di pagamento che sto cancellando:$paymentMethodId")
        println("userId: $userId")
        return checkoutApiService.deletePaymentMethod(userId, paymentMethodId)
    }

    suspend fun getPaymentMethod(userId: UUID, paymentMethodId: Long) : Response<PaymentMethod?> {
        return checkoutApiService.getPaymentMethod(userId, paymentMethodId)
    }

    suspend fun confirmOrder(checkoutRequest: CheckoutRequest) : Response<SaveOrder>
    {

        return checkoutApiService.addOrder(checkoutRequest)
    }
}
