package com.hfad.antiplag.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.antiplag.data.PlagiarismCheckManager
import com.hfad.antiplag.data.PlagiatService
import com.hfad.antiplag.model.Report
import com.hfad.antiplag.model.ReportResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlagiarismCheckViewModel : ViewModel() {
    private val plagiatService = PlagiatService()
    private val checkManager = PlagiarismCheckManager(plagiatService)

    private val _report = MutableStateFlow<ReportResponse?>(null)
    val report = _report.asStateFlow()

    val checkState = checkManager.state
    fun checkText(text: String) {
        viewModelScope.launch {
            checkManager.checkTextAndGetReport(text)
        }

    }

    fun resetCheck() {
        viewModelScope.launch {
            checkManager.reset()
        }
    }

    fun updateReport(newReport: ReportResponse){
        _report.value = newReport
    }
}