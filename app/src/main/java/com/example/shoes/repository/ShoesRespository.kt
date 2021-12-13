package com.example.shoes.repository

import com.example.shoes.model.Response
import com.example.shoes.model.ShoesResponse
import com.example.shoes.model.UserResponse
import com.example.shoes.network.ApiInterface
import com.example.shoes.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class ShoesRespository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    suspend fun getShoesResponse(): Resource<List<ShoesResponse>> {
        val response = try {
            apiInterface.getShoesData()
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured: ${e.localizedMessage}")
        }

        return Resource.Success(response)
    }
    suspend fun getShoeResponse(id:Long): Resource<ShoesResponse> {
        val response = try {
            apiInterface.getShoeData(id)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured: ${e.localizedMessage}")
        }

        return Resource.Success(response)
    }
    suspend fun login(userName:String, password:String): Resource<UserResponse> {
        val response = try {
            apiInterface.login(userName,password)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured: ${e.localizedMessage}")
        }
        return Resource.Success(response)
    }
    suspend fun register(userName:String, password:String, email:String): Resource<Response> {
        val response = try {
            apiInterface.register(userName,password, email)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured: ${e.localizedMessage}")
        }
        return Resource.Success(response)
    }
}