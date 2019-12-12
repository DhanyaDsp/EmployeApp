package com.ey.pwbc.ui.vetrina

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VetrinaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Vetrina View"
    }
    val text: LiveData<String> = _text
}