package com.danny.vision.viewmodels

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.danny.vision.models.LoginRequest

class LoginViewModel (application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val _currentPhoneNumber = mutableStateOf<LoginRequest?>(null)
    val currentPhoneNumber: State<LoginRequest?> = _currentPhoneNumber
    // login func to call otp api
}