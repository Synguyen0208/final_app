package com.example.shoes.network

import com.example.shoes.model.Response
import com.example.shoes.model.ShoesResponse
import com.example.shoes.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ApiInterface {

    @GET("wc/v3/products")
    suspend fun getShoesData(): List<ShoesResponse>

    @GET("wc/v3/products/{shoe_id}")
    suspend fun getShoeData(
        @Path("shoe_id")shoe_id:Long
    ): ShoesResponse

    @GET("wp/login/")
    suspend fun login(
    @Query("username") username:String,
    @Query("password") password:String,
    ):UserResponse

    @POST("wp/register/")
    suspend fun register(
        @Query("username") username:String,
        @Query("password") password:String,
        @Query("email") email:String,
    ):Response
}