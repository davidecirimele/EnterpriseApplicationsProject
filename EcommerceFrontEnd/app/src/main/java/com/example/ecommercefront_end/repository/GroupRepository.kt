package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Group
import com.example.ecommercefront_end.network.GroupApiService
import java.util.UUID
import retrofit2.Response

class GroupRepository (private val groupApiService: GroupApiService){


    suspend fun getGroupById(userId: UUID, groupId: Long): Group {
        return groupApiService.getGroupById(userId, groupId)
    }

    suspend fun updateGroup(groupId: Long, group: Group): Response<Group> {
        return groupApiService.updateGroup(groupId, group)
    }


    suspend fun addUser(userId: UUID, token: String): Response<Boolean> {
        return groupApiService.addUserToGroup(userId, token)
    }

    suspend fun removeUser(groupId: Long, userId: UUID): Response<Boolean> {
        return groupApiService.removeUserFromGroup(groupId, userId)
    }

    suspend fun deleteGroup(groupId: Long): Response<Unit> {
        return groupApiService.deleteGroup(groupId)
    }




}