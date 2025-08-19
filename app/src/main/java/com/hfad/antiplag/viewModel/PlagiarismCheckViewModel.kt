package com.hfad.antiplag.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.antiplag.data.PlagiarismCheckManager
import com.hfad.antiplag.data.PlagiatService
import kotlinx.coroutines.launch

class PlagiarismCheckViewModel : ViewModel() {
    private val plagiatService = PlagiatService()
    private val checkManager = PlagiarismCheckManager(plagiatService)
    val checkState = checkManager.state
    fun checkText(text: String) {
        viewModelScope.launch {
            checkManager.checkText(text)
        }

    }

    fun resetCheck() {
        viewModelScope.launch {
            checkManager.reset()
        }
    }
}