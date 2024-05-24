package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem

class StoryViewModel(private val repository: UserRepository) : ViewModel() {

    private var userToken: String = ""
    private val _listStory = MutableLiveData<PagingData<ListStoryItem>>()
    val listStory: LiveData<PagingData<ListStoryItem>> get() = _listStory
    init {
        fetchData()
    }
    fun setToken(token: String) {
        userToken = token
        fetchData()
    }
    private fun fetchData() {
        if (userToken.isNotEmpty()) {
            repository.userStories(userToken)
                .cachedIn(viewModelScope)
                .observeForever {
                    _listStory.value = it
                }
        }
    }
}
