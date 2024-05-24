package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository):ViewModel() {
    private val _storyListwithLocation = MutableLiveData<List<ListStoryItem>>()
    val storyListwithLocation: LiveData<List<ListStoryItem>> = _storyListwithLocation

    fun getStorieswithLocation(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserStoriesLocation("Bearer $token")
                if (response.error == false) {
                    _storyListwithLocation.postValue(response.listStory as List<ListStoryItem>?)
                }
            } catch (e: Exception) {
                Log.e("MapsActivity", "Error: ${e.message}")
            }
        }
    }
}