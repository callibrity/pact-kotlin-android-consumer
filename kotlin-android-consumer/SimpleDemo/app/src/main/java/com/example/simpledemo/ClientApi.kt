package com.example.simpledemo

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ClientApi {

    @GET("v1/users/{id}")
    suspend fun getUser(@Path("id") id: String,
                        @Header("Authorization") authHeader: String):
            Response<UserResponse>

    @GET("v1/users")
    suspend fun getUsers(@Header("Authorization") authHeader: String):
            Response<UserResponseList>

    @GET("v1/users")
    suspend fun getUsers(@Query("firstName") firstNames: String,
                         @Header("Authorization") authHeader: String):
            Response<UserResponseList>

    @GET("v1/users")
    suspend fun getUsers(@Query("firstName") firstNames: String,
                         @Query("lastName") lastNames: String,
                         @Header("Authorization") authHeader: String):
            Response<UserResponseList>

    @POST("v1/users")
    suspend fun addUser(@Body body: UserRequest,
                        @Header("Authorization") authHeader: String): Response<UserResponse>
}