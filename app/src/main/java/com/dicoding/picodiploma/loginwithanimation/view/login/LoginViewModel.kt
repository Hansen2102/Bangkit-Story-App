package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>>
        get() = _loginResult

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (response.error == false && response.loginResult != null) {
                    val token = response.loginResult.token ?: ""
                    _token.value = token
                    _loginResult.value = Pair(true, response.message)
                } else {
                    _loginResult.value = Pair(false, response.message)
                }
            } catch (e: HttpException) {
                val errorMessage = e.message()
                _loginResult.value = Pair(false, errorMessage)
            }
        }
    }
}