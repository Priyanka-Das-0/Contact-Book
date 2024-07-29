package com.example.contact

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class TempViewModel : ViewModel() {
    var a =1
    fun  provideA(): Int {
        return a
    }

}