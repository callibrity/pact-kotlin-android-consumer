package com.example.simpledemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
}