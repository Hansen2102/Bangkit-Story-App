import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _signupResult = MutableLiveData<Pair<Boolean, String?>>()
    val signupResult: LiveData<Pair<Boolean, String?>>
        get() = _signupResult

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.register(name, email, password)
                when {
                    response.error == false -> {
                        _signupResult.value = Pair(true, response.message)
                    }
                    else -> {
                        _signupResult.value = Pair(false, response.message)
                    }
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _signupResult.value = Pair(false, errorMessage)
            }
        }
    }
}
