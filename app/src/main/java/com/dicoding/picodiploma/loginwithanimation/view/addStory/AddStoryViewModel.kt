package com.dicoding.picodiploma.loginwithanimation.view.addStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _uploadResult = MutableLiveData<Pair<Boolean, String?>>()
    val uploadResult: LiveData<Pair<Boolean, String?>>
        get() = _uploadResult

    fun uploadImage(file: MultipartBody.Part,token:String,description: RequestBody) {
        viewModelScope.launch {
            try {
                val response = repository.uploadImage(file,"Bearer $token",description)
                when {
                    !response.error -> {
                        _uploadResult.value = Pair(true, response.message)
                    }
                    else -> {
                        _uploadResult.value = Pair(false, response.message)
                    }
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _uploadResult.value = Pair(false, errorMessage)
            }
        }
    }
}