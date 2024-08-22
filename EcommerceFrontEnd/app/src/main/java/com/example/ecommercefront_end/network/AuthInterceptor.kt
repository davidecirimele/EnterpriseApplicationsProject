package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.RequiresAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class AuthInterceptor(
    private val sessionManager: SessionManager,
    private val authApiService: AuthApiService
) : Interceptor {

    override   fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requiresAuth = originalRequest.tag(Invocation::class.java)?.method()?.getAnnotation(
            RequiresAuth::class.java) != null

        if (!requiresAuth) {

            val accessToken = sessionManager.authToken

            val requestBuilder = originalRequest.newBuilder()
            accessToken?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }

            val requestWithAuth = requestBuilder.build()
            val response = chain.proceed(requestWithAuth)

            if (response.code == 401) {


                    val refreshToken = sessionManager.refreshToken
                    if (refreshToken != null) {
                        val newTokensResponse = runBlocking { withContext(Dispatchers.IO) {authApiService.refreshToken(refreshToken)  } }
                        if (newTokensResponse.isSuccessful) {
                            val newAccessToken = newTokensResponse.body()?.accessToken
                            val newRefreshToken = newTokensResponse.body()?.refreshToken

                            if (newAccessToken != null && newRefreshToken != null) {
                                sessionManager.saveAuthToken(newAccessToken)
                                sessionManager.saveRefreshToken(newRefreshToken)

                                val newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer $newAccessToken")
                                    .build()

                                response.close()

                                return chain.proceed(newRequest)
                            } else {
                                // Se il refresh token non funziona, logout o richiedi il login
                                sessionManager.clearSession()
                                // Puoi anche lanciare un'eccezione o gestire il logout in altro modo
                                //FIXME logout + redirect to login
                            }
                        }
                    }
                }


        return response
    }
        else {
            return chain.proceed(originalRequest)
        }
    }
}
