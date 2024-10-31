package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Group
import com.example.ecommercefront_end.model.RequiresAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface GroupApiService {

    @GET("groups/getById/{idUser}/{idGroup}")
    @RequiresAuth
    suspend fun getGroupById( @Path("idUser") idUser: UUID, @Path("idGroup") idGroup: Long): Group

    @PUT("groups/{groupId}")
    @RequiresAuth
    suspend fun updateGroup( @Path("groupId") groupId: Long, @Body group: Group): Response<Group>

    @POST("groups/add")
    @RequiresAuth
    suspend fun addGroup(@Body groupDto: Group): Group

    @POST("groups/addUser/{idUser}/{token}")
    @RequiresAuth
    suspend fun addUserToGroup( @Path("idUser") idUser: UUID, @Path("token") token: String): Response<Boolean>

    @DELETE("groups/removeUser/{idGroup}/{idUser}")
    @RequiresAuth
    suspend fun removeUserFromGroup( @Path("idGroup") idGroup: Long, @Path("idUser") idUser: UUID): Response<Boolean>

    @DELETE("groups/{groupId}")
    @RequiresAuth
    suspend fun deleteGroup(@Path("groupId") groupId: Long): Response<Unit>
}

// create wishlist, remove items