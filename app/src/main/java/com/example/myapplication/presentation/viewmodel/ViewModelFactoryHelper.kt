package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

fun <VM: ViewModel> viewModelFactory(initializer: (CreationExtras) -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return initializer(extras) as T
        }
    }
}