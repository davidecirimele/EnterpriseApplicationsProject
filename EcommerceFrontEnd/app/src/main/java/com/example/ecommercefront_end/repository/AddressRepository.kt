package com.example.ecommercefront_end.repository

import android.util.Log
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.network.AddressApiService
import retrofit2.Response
import java.util.UUID

class AddressRepository(private val apiService : AddressApiService)  {

    suspend fun getAddress(userId : UUID, addressId: Long) = apiService.getAddress(userId, addressId)

    suspend fun getAddresses(userId : UUID) : Response<List<Address>> = apiService.getAddresses(userId)

    suspend fun insertAddress(userId : UUID, address: SaveAddress) = apiService.insertAddress(userId, address)

    suspend fun deleteAddress(userId : UUID, addressId: Long)= apiService.removeAddress(addressId, userId)

    suspend fun updateDefaultAddress(userId : UUID, addressId: Long) = apiService.updateDefaultAddress(addressId, userId)

    suspend fun getDefaultAddress(userId: UUID) : Response<Address?> = apiService.getDefaultAddress(userId)

    suspend fun editAddress(userId: UUID, addressId: Long, address: SaveAddress) = apiService.editAddress(userId, addressId, address)
}