package com.example.shoes.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoes.model.Response
import com.example.shoes.model.ShoesResponse
import com.example.shoes.model.UserResponse
import com.example.shoes.repository.ShoesRespository
import com.example.shoes.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoesViewModel @Inject constructor(
    private val shoesRespository: ShoesRespository,
): ViewModel() {

    var isLoading = mutableStateOf(false)
    private var _getShoesData: MutableLiveData<List<ShoesResponse>> = MutableLiveData<List<ShoesResponse>>()
    var getShoesData: LiveData<List<ShoesResponse>> = _getShoesData

    suspend fun getShoesData(): Resource<List<ShoesResponse>> {
        val result = shoesRespository.getShoesResponse()
        if (result is Resource.Success) {
            isLoading.value = true
            _getShoesData.value = result.data!!
        }

        return result
    }
    suspend fun getShoeData(id:Long): Resource<ShoesResponse> {
        val result = shoesRespository.getShoeResponse(id)
        if (result is Resource.Success) {
            isLoading.value = true
        }
        return result
    }
    suspend fun login(username:String, password:String): Resource<UserResponse> {
        val result = shoesRespository.login(username, password)
        if (result is Resource.Success) {
            isLoading.value = true
        }
        return result
    }
    suspend fun register(username:String, password:String, email:String): Resource<Response> {
        val result = shoesRespository.register(username, password, email)
        if (result is Resource.Success) {
            isLoading.value = true
        }
        return result
    }
}